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

package com.inu.cafeteria.common.base

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import com.inu.cafeteria.R
import com.inu.cafeteria.common.extension.setVisible
import kotlinx.android.synthetic.main.web_view_only_fragment.view.*

abstract class WebViewOnlyFragment : BaseFragment() {

    abstract fun getPageUrl(): String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.web_view_only_fragment, container, false).apply {
            initializeView(this)
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initializeView(view: View) {
        with(view.web_view) {
            webChromeClient = object: WebChromeClient() {
                override fun onProgressChanged(webView: WebView?, newProgress: Int) {
                    with(view.progress_bar) {
                        progress = newProgress
                        if (newProgress == 100) {
                            setVisible(false)
                        }
                    }
                }
            }

            with(settings) {
                javaScriptEnabled = true
                defaultTextEncodingName = "utf-8"
            }

            loadUrl(getPageUrl())
        }
    }
}