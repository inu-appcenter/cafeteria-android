/**
 * This file is part of INU Cafeteria.
 *
 * Copyright (C) 2020 INU Global App Center <potados99@gmail.com>
 *
 * INU Cafeteria is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * INU Cafeteria is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.inu.cafeteria.feature.discount

import android.graphics.Bitmap
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.fragment.app.viewModels
import com.inu.cafeteria.R
import com.inu.cafeteria.common.base.BaseFragment
import com.inu.cafeteria.common.extension.*
import com.inu.cafeteria.common.navigation.Navigator
import com.inu.cafeteria.databinding.DiscountFragmentBinding
import it.sephiroth.android.library.xtooltip.Tooltip
import org.koin.core.inject

class DiscountFragment : BaseFragment() {

    private lateinit var binding: DiscountFragmentBinding
    private val viewModel: DiscountViewModel by viewModels()

    private val navigator: Navigator by inject()

    override fun onNetworkStateChange(available: Boolean) {
        if (available) {
            viewModel.load()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Should be performed whether the networks is available or not.
        viewModel.preload()
    }

    override fun onCreateView(viewCreator: ViewCreator): View =
        viewCreator<DiscountFragmentBinding> {
            initializeView(this)
            vm = viewModel
            binding = this
        }

    private fun initializeView(binding: DiscountFragmentBinding) {
        setToolbar(R.id.toolbar_discount, R.menu.discount_menu)

        with(binding.carrot) {
            startAnimation(AnimationUtils.loadAnimation(context, R.anim.shake_forever).apply {
                duration = 200
            })
        }

        with(viewModel) {
            observe(loggedInStatus) {
                viewModel.load()
            }

            observe(bright) {
                applyBrightness()
            }

            observe(showBrightnessToggleHintEvent) {
                it ?: return@observe

                with(binding.barcodeCardPart.card) {
                    postDelayed({
                        // We need some time to get the layout ready, before we show the hint.
                        showTooltip(context, binding.root, Tooltip.Gravity.TOP, it.hintText) {
                            viewModel.markHintShown()
                        }
                    }, 500)
                }
            }

            observe(showDiscountServiceDescriptionEvent) {
                navigator.showDialog(activity ?: return@observe, "", "")
            }
        }
    }

    override fun onResume() {
        super.onResume()

        applyBrightness()

        viewModel.emitHintEvent()
    }

    override fun onPause() {
        super.onPause()

        resetBrightness()
        clearTooltip()
    }

    private fun applyBrightness() {
        if (viewModel.bright.value == true) {
            activity?.setBrightness(1f)
        } else {
            activity?.resetBrightness()
        }
    }

    private fun resetBrightness() {
        activity?.resetBrightness()
    }

    private fun clearTooltip() {
        binding.root.dismissTooltip()
    }

    override fun onOptionsItemSelected(item: MenuItem) =
        viewModel.onClickOptionMenu(item.itemId)

    companion object {

        @JvmStatic
        @BindingAdapter("barcodeImage")
        fun setBarcodeImage(view: ImageView, barcodeImage: Bitmap?) {
            with(view) {
                setImageBitmap(barcodeImage)
                setVisible(barcodeImage != null)
            }
        }
    }
}