package com.inu.cafeteria.feature.main

import android.view.ViewGroup
import com.inu.cafeteria.R
import com.inu.cafeteria.common.base.BaseViewHolder
import com.inu.cafeteria.common.base.DefaultAdapter
import com.inu.cafeteria.common.extension.setVisible
import kotlinx.android.synthetic.main.menu.view.*
import timber.log.Timber

class MenuAdapter : DefaultAdapter<MenuView>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        Timber.d("Inflate Menu view holder!")

        return BaseViewHolder(parent, R.layout.menu)
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        val menu = getItem(position) ?: return

        with(holder.view) {

            with(available_at) {
                setAvailableTime(menu.availableAt)
            }

            with(corner_name) {
                text = menu.cornerName
            }

            with(foods) {
                text = menu.foods

                // Initial
                maxLines = 2

                // On click
                setOnClickListener { maxLines = 5 }
            }

            with(price) {
                setVisible(menu.price ?: 0 != 0)
                text = context.getString(R.string.unit_krw, menu.price)
            }

            with(calorie) {
                setVisible(menu.calorie ?: 0 != 0)
                text = context.getString(R.string.unit_cal, menu.calorie)
            }
        }
    }
}