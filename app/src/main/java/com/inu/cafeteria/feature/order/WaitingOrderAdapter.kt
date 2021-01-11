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

import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.DiffUtil
import com.inu.cafeteria.R
import com.inu.cafeteria.common.base.GenericAdapter
import com.inu.cafeteria.databinding.WaitingOrderItemBinding

class WaitingOrderAdapter : GenericAdapter<WaitingOrderView, WaitingOrderItemBinding>() {

    var onClickDelete: (Int) -> Unit = {}

    override fun onItemsChanged(old: List<WaitingOrderView>, new: List<WaitingOrderView>) {
        val diffCallback = WaitingOrderDiffCallback(old, new)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        diffResult.dispatchUpdatesTo(this)
    }

    override fun getLayoutIdForPosition(position: Int) = R.layout.waiting_order_item

    override fun onBindFinished(item: WaitingOrderView, holder: GenericViewHolder) {
        // Do additional jobs here, including setting click listeners.

        with(holder.binding.closeButton) {
            setOnClickListener {
                onClickDelete(item.orderId)
            }
        }
        
        with(holder.binding.number) {
            clearAnimation()

            if (item.done) {
                val animation = AnimationUtils.loadAnimation(context, R.anim.alpha_animation)

                startAnimation(animation)
            }
        }
    }
}