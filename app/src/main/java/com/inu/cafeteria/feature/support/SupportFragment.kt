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

import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.view.View
import android.widget.Toast
import androidx.databinding.BindingAdapter
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.inu.cafeteria.R
import com.inu.cafeteria.common.base.BaseFragment
import com.inu.cafeteria.common.extension.setVisible
import com.inu.cafeteria.databinding.SupportFragmentBinding

class SupportFragment : BaseFragment() {

    private val viewModel: SupportViewModel by viewModels()

    private val adapter = SupportOptionsAdapter()

    override fun onCreateView(viewCreator: ViewCreator): View {
        return viewCreator.createView<SupportFragmentBinding> {
            initializeView(this)
            vm = viewModel
        }
    }

    private fun initializeView(binding: SupportFragmentBinding) {
        with(binding.supportOptionsRecycler) {
            adapter = this@SupportFragment.adapter
        }

        with(adapter) {
            onClickRootLayout = {
                findNavController().navigate(it.navigateTo)
            }
        }

        with(binding.kakaotalkButtonPart) {
            setVisible(isKakaoTalkInstalled())
            setOnClickListener { onKakaoTalkClick() }
        }

        with(binding.uicoopCallButtonPart) {
            setOnClickListener { onCallUiCoopClick() }
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

    private fun onKakaoTalkClick() {
        safeStartActivity(viewModel.getKakaoIntent())
    }

    private fun onCallUiCoopClick() {
        safeStartActivity(viewModel.callUiCoopIntent())
    }

    private fun safeStartActivity(intent: Intent) {
        try {
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(activity, getText(R.string.fail_no_activity), Toast.LENGTH_SHORT).show()
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