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
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import androidx.databinding.BindingAdapter
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.messaging.RemoteMessage
import com.inu.cafeteria.R
import com.inu.cafeteria.common.base.BaseFragment
import com.inu.cafeteria.common.navigation.Navigator
import com.inu.cafeteria.common.service.CafeteriaFirebaseMessagingService
import com.inu.cafeteria.databinding.WaitingOrderFragmentBinding
import org.koin.core.inject
import kotlin.concurrent.timer

class WaitingOrderFragment : BaseFragment() {

    private val viewModel: WaitingOrderViewModel by viewModels()
    private lateinit var binding: WaitingOrderFragmentBinding

    private val navigator: Navigator by inject()

    /** Receives in-app firebase notification when app is active */
    private val pushNumberNotificationReceiver = object: BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            handleNotificationMessage(
                intent?.getParcelableExtra("message") ?: return
            )
        }
    }

    private val pushNumberNotificationFilter = IntentFilter().apply {
        addAction(CafeteriaFirebaseMessagingService.ACTION_PUSH_NUMBER_NOTIFICATION)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        context?.registerReceiver(
            pushNumberNotificationReceiver,
            pushNumberNotificationFilter
        )
    }

    override fun onCreateView(viewCreator: ViewCreator): View {
        return viewCreator.createView<WaitingOrderFragmentBinding> {
            initializeView(this)
            binding = this
            vm = viewModel
        }
    }

    override fun onResume() {
        super.onResume()
        clearAllOrderNotifications()

        viewModel.fetchWaitingOrders()
    }

    override fun onDestroy() {
        super.onDestroy()

        context?.unregisterReceiver(pushNumberNotificationReceiver)
    }

    private fun initializeView(binding: WaitingOrderFragmentBinding) {
        with(binding.waitingOrdersRecycler) {
            adapter = WaitingOrderAdapter().apply {
                emptyView = binding.emptyView
                loadingView = binding.loadingView

                onClickDelete = viewModel::deleteWaitingOrder
            }
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }

        with(binding.addOrderButton) {
            setOnClickListener {
                navigator.showAddWaitingOrder()
            }
        }

        periodicallyShakeAddButton(3000)
    }

    private fun periodicallyShakeAddButton(interval: Long) {
        timer(period = interval) {
            val orders = viewModel.orders.value ?: return@timer

            if (orders.isNotEmpty()) {
                return@timer
            }

            with(binding.addOrderButton) {
                startAnimation(AnimationUtils.loadAnimation(context, R.anim.shake_once).apply {
                    duration = 100
                })
            }
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

    private fun handleNotificationMessage(message: RemoteMessage) {
        val orderId = message.data["order_id"]?.toInt() ?: return

        viewModel.markOrderReady(orderId)

        navigator.showOrderFinishedNotification(activity ?: return) {
            viewModel.fetchWaitingOrders()
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