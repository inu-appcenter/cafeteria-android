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

package com.inu.cafeteria.feature.order

import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.view.View
import android.view.animation.AnimationUtils
import androidx.databinding.BindingAdapter
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.inu.cafeteria.R
import com.inu.cafeteria.common.base.BaseFragment
import com.inu.cafeteria.common.navigation.Navigator
import com.inu.cafeteria.databinding.WaitingOrderFragmentBinding
import org.koin.core.inject

class WaitingOrderFragment : BaseFragment() {

    private val viewModel: WaitingOrderViewModel by viewModels()
    private lateinit var binding: WaitingOrderFragmentBinding

    private val navigator: Navigator by inject()

    override fun onCreateView(viewCreator: ViewCreator): View {
        return viewCreator.createView<WaitingOrderFragmentBinding> {
            initializeView(this)
            binding = this
            vm = viewModel
        }
    }

    private fun initializeView(binding: WaitingOrderFragmentBinding) {
        with(binding.waitingOrdersRecycler) {
            adapter = WaitingOrderAdapter().apply {
                emptyView = binding.emptyView
                loadingView = binding.loadingView
            }
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }

        with(binding.addOrderButton) {
            setOnClickListener {
                navigator.showAddWaitingOrder()
            }
        }
    }

    override fun onResume() {
        super.onResume()

        emphasizeAddButton()
        clearAllOrderNotifications()

        viewModel.fetchWaitingOrders()
    }

    private fun emphasizeAddButton() {
        with(binding.addOrderButton) {
            startAnimation(AnimationUtils.loadAnimation(context, R.anim.shake_once).apply {
                duration = 100
            })
        }
    }

    private fun clearAllOrderNotifications() {
        // TODO: this does not work.
        val notificationManager = activity?.getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager ?: return

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Cancel notifications only on push number notification channel
            val channelIdToClearNotifications = getString(R.string.push_number_notification_channel_id)

            notificationManager.activeNotifications.forEach {
                if (it.notification.channelId == channelIdToClearNotifications) {
                    notificationManager.cancel(it.id)
                }
            }
        } else {
            // Cancel all notifications
            notificationManager.cancelAll()
        }
    }

    companion object {
        @JvmStatic
        @BindingAdapter("waitingOrders")
        fun setWaitingOrders(view: RecyclerView, orders: List<WaitingOrderView>?) {
            orders ?: return

            (view.adapter as? WaitingOrderAdapter)?.items = orders
        }
    }
}