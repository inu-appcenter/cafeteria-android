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

package com.inu.cafeteria.feature.barcode

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.inu.cafeteria.R
import com.inu.cafeteria.common.base.BaseFragment
import com.inu.cafeteria.common.extension.getViewModel
import com.inu.cafeteria.databinding.BarcodeFragmentBinding
import kotlinx.android.synthetic.main.barcode_fragment.*
import kotlinx.android.synthetic.main.barcode_fragment.view.*

class BarcodeFragment : BaseFragment() {

    private lateinit var viewDataBinding: BarcodeFragmentBinding
    private lateinit var barcodeViewModel: BarcodeViewModel

    init {
        failables += this
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        barcodeViewModel = getViewModel()
        failables += barcodeViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return BarcodeFragmentBinding
            .inflate(inflater, container, false)
            .apply { vm = barcodeViewModel }
            .apply { lifecycleOwner = this@BarcodeFragment }
            .apply { viewDataBinding = this }
            .apply { initializeView(root) }
            .root
    }

    override fun onResume() {
        super.onResume()

        with(barcodeViewModel) {
            tryActivatingBarcode(
                onSuccess = {
                    // The view must exist after barcode is made.
                    barcode_image ?: return@tryActivatingBarcode

                    with(barcode_image) {
                        setImageBitmap(it)
                        invalidate()
                    }
                    brightenScreen(activity)
                },
                onFail = ::handleActivateBarcodeFailure,
                onNoBarcode = { fail(R.string.fail_no_barcode) }
            )
        }
    }

    override fun onPause() {
        super.onPause()

        with(barcodeViewModel) {
            tryInvalidatingBarcode(
                onFail = ::handleActivateBarcodeFailure,
                onNoBarcode = { fail(com.inu.cafeteria.R.string.fail_no_barcode) }
            )
            restoreScreen(activity)
        }
    }

    private fun initializeView(view: View) {
        with(view.close_button) {
            setOnClickListener {
                activity?.finish()
            }
        }
    }
}