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
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.fragment.app.viewModels
import com.inu.cafeteria.R
import com.inu.cafeteria.common.base.BaseFragment
import com.inu.cafeteria.common.extension.*
import com.inu.cafeteria.databinding.DiscountFragmentBinding
import it.sephiroth.android.library.xtooltip.Tooltip

class DiscountFragment : BaseFragment() {

    private val viewModel: DiscountViewModel by viewModels()

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
        }

    private fun initializeView(binding: DiscountFragmentBinding) {
        with(binding.carrot) {
            startAnimation(AnimationUtils.loadAnimation(context, R.anim.shake_forever).apply {
                duration = 200
            })
        }

        with(viewModel) {
            observe(loggedInStatus) {
                if (isOnline()) {
                    // Same pattern as in MainActivity.
                    viewModel.load()
                }
            }

            observe(bright) {
                applyBrightness()
            }

            observe(showBrightnessToggleHint) {
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
        }
    }

    override fun onResume() {
        super.onResume()

        viewModel.emitHintEvent()
        applyBrightness()
    }

    override fun onPause() {
        super.onPause()

        resetBrightness()
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