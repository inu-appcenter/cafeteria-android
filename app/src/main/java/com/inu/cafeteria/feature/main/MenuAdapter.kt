package com.inu.cafeteria.feature.main

import android.view.ViewGroup
import com.inu.cafeteria.R
import com.inu.cafeteria.common.base.BaseAdapter
import com.inu.cafeteria.common.base.BaseViewHolder
import kotlinx.android.synthetic.main.menu.view.*
import timber.log.Timber

class MenuAdapter : BaseAdapter<MenuView>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        Timber.d("Inflate Menu view holder!")

        return BaseViewHolder(parent, R.layout.menu)
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        val menu = getItem(position) ?: return

        with(holder.containerView) {
            // TODO
            available_at.setAvailableTime((1..7).random())
            corner_name.text = menu.cornerName
            price.text = kotlin.String.format("%,d원", menu.price)
            calorie.text = kotlin.String.format("%,dkcal", menu.calorie)

            // TODO: dirty. clean it.
            with(foods) {
                maxLines = 2
                text = menu.foods
            }

            setOnClickListener { foods.maxLines = 5 }
        }
    }
}