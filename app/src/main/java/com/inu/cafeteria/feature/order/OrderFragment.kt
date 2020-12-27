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
import android.content.Intent
import android.os.Build
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.inu.cafeteria.R
import com.inu.cafeteria.common.base.BaseFragment
import com.inu.cafeteria.databinding.OrderFragmentBinding

class OrderFragment : BaseFragment() {

    private val viewModel: OrderViewModel by viewModels()

    override fun onCreateView(viewCreator: ViewCreator): View {
        return viewCreator.createView<OrderFragmentBinding> {
            initializeView(this)
            vm = viewModel
        }
    }

    override fun onResume() {
        // Check all notifications
        super.onResume()
        clearAllOrderNotifications()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 99 && resultCode == 1) {
            val number = data?.getIntExtra("num", -1) ?: return

            Toast.makeText(activity, "$number", Toast.LENGTH_SHORT).show()
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

    private fun initializeView(binding: OrderFragmentBinding) {
        binding.openCameraButton.setOnClickListener {
            startActivityForResult(Intent(activity, AddOrderActivity::class.java), 99)
        }
    }
}