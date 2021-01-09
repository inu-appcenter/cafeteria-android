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

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
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
import com.inu.cafeteria.common.extension.observe
import com.inu.cafeteria.common.navigation.Navigator
import com.inu.cafeteria.common.review.ReviewHelper
import com.inu.cafeteria.common.service.CafeteriaFirebaseMessagingService
import com.inu.cafeteria.databinding.WaitingOrderFragmentBinding
import org.koin.core.inject

class WaitingOrderFragment : BaseFragment() {

    private val viewModel: WaitingOrderViewModel by viewModels()
    private lateinit var binding: WaitingOrderFragmentBinding

    private val navigator: Navigator by inject()

    /** Receives in-app firebase notification when app is active */
    private val pushNumberNotificationReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val message: RemoteMessage = intent?.getParcelableExtra("message") ?: return

            viewModel.handleNotification(message)
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

    override fun onCreateView(create: ViewCreator): View {
        return create.createView<WaitingOrderFragmentBinding> {
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

                onClickDelete = viewModel::deleteWaitingOrder
            }

            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }

        with(viewModel) {
            observe(shakeAddButtonEvent) {
                val hasOrders = (viewModel.orders.value?.isNotEmpty() == true)
                if (hasOrders) {
                    return@observe
                }

                // Start shake animation
                with(binding.addOrderButton) {
                    startAnimation(AnimationUtils.loadAnimation(context, R.anim.shake_once).apply {
                        duration = 150
                    })
                }
            }

            observe(showOrderReadyDialogEvent) {
                it ?: return@observe

                navigator.showOrderFinishedNotification(activity ?: return@observe, it.first, it.second) {
                    viewModel.askForReviewIfAllOrdersFinished()
                }
            }

            observe(askForReviewEvent) {
                ReviewHelper(activity ?: return@observe).askForReview {
                    markAskedForReview()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()

        viewModel.clearAllOrderNotifications()
        viewModel.fetchWaitingOrders()
    }

    override fun onPause() {
        super.onPause()

        viewModel.deleteFinishedOrders()
    }

    override fun onDestroy() {
        super.onDestroy()

        context?.unregisterReceiver(pushNumberNotificationReceiver)
    }

    companion object {
        @JvmStatic
        @BindingAdapter("waitingOrders")
        fun setWaitingOrders(view: RecyclerView, orders: List<WaitingOrderView>?) {
            orders ?: return

            (view.adapter as? WaitingOrderAdapter)?.items = orders
        }

        @JvmStatic
        @BindingAdapter("areOrdersLoading")
        fun setAreOrdersLoading(view: RecyclerView, isLoading: Boolean?) {
            isLoading ?: return

            (view.adapter as? WaitingOrderAdapter)?.isLoading = isLoading
        }
    }
}