/**
 * Copyright (C) 2018-2019 INU Appcenter. All rights reserved.
 *
 * This file is part of INU Cafeteria.
 *
 * This work is licensed under the terms of the MIT license.
 * For a copy, see <https://opensource.org/licenses/MIT>.
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