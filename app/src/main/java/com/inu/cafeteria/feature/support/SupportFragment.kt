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

package com.inu.cafeteria.feature.support

import android.content.pm.PackageManager
import android.view.View
import androidx.databinding.BindingAdapter
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.inu.cafeteria.common.base.BaseFragment
import com.inu.cafeteria.common.extension.setVisible
import com.inu.cafeteria.databinding.SupportFragmentBinding

class SupportFragment : BaseFragment<SupportFragmentBinding>() {

    private val viewModel: SupportViewModel by viewModels()

    override fun onCreateView(create: ViewCreator): View {
        return create.createView<SupportFragmentBinding> {
            initializeView(this)
            vm = viewModel
        }
    }

    private fun initializeView(binding: SupportFragmentBinding) {
        with(binding.supportOptionsRecycler) {
            adapter = SupportOptionsAdapter().apply {
                onClickRootLayout = {
                    this@SupportFragment.findNavController().navigate(it.navigateTo)
                }
            }
        }

        with(binding.kakaotalkButtonPart.kakaotalkButton) {
            setVisible(isKakaoTalkInstalled())
            setOnClickListener { viewModel.openKakaoTalk() }
        }

        with(binding.uicoopCallButtonPart.callUicoopButton) {
            setOnClickListener { viewModel.callUiCoop() }
        }
    }

    private fun isKakaoTalkInstalled(): Boolean {
        return try {
            activity?.packageManager?.getPackageInfo("com.kakao.talk", PackageManager.GET_ACTIVITIES)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }

    companion object {

        @JvmStatic
        @BindingAdapter("supportOptions")
        fun setSupportOptions(view: RecyclerView, options: List<SupportOption>?) {
            options?.let {
                (view.adapter as? SupportOptionsAdapter)?.items = it
            }
        }
    }
}