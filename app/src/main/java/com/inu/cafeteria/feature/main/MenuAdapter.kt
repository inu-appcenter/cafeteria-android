package com.inu.cafeteria.feature.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.inu.cafeteria.R
import kotlinx.android.synthetic.main.menu.view.*
import timber.log.Timber

class MenuAdapter : RecyclerView.Adapter<MenuAdapter.TheViewHolder>() {

    var pagedMenus: List<MenuView> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TheViewHolder {
        return TheViewHolder(parent)
    }

    override fun onBindViewHolder(holder: TheViewHolder, position: Int) {
        holder.bind(pagedMenus[position])
    }

    override fun getItemCount(): Int {
        return pagedMenus.size
    }

    class TheViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        constructor(parent: ViewGroup) : this(LayoutInflater.from(parent.context).inflate(R.layout.menu, parent, false))

        init {
            Timber.i("Inflate Menu!")
        }

        fun bind(menu: MenuView) {
            with(itemView) {
                available_at.setAvailableTime(7)
                foods.text = menu.foods
                corner_name.text = menu.cornerName
                price.text = String.format("%,d원", menu.price)
                calorie.text = String.format("%,dkcal", menu.calorie)
            }
        }
    }
}