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
import com.inu.cafeteria.common.EventHub
import com.inu.cafeteria.common.base.BaseFragment
import com.inu.cafeteria.common.extension.observe
import com.inu.cafeteria.common.extension.setVisible
import com.inu.cafeteria.databinding.DiscountFragmentBinding
import kotlinx.android.synthetic.main.discount_fragment.view.*
import org.koin.core.inject

class DiscountFragment : BaseFragment() {

    private val viewModel: DiscountViewModel by viewModels()
    private val eventHub: EventHub by inject()

    override fun onNetworkChange(available: Boolean) {
        if (available) {
            viewModel.load()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Offline works. Should be performed whether the networks is available or not.
        viewModel.preload()
    }

    override fun onCreateView(viewCreator: ViewCreator): View =
        viewCreator<DiscountFragmentBinding> {
            initializeView(root)
            vm = viewModel
        }

    private fun initializeView(view: View) {
        with(view.carrot) {
            startAnimation(AnimationUtils.loadAnimation(context, R.anim.shake_forever).apply {
                duration = 200
            })
        }

        with(eventHub) {
            observe(loginEvent) {
                viewModel.load()
            }
        }
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