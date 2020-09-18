package com.inu.cafeteria.feature.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.inu.cafeteria.R

class MenuAdapter : RecyclerView.Adapter<MenuAdapter.TheViewHolder>() {

    var pagedMenus: List<String> = listOf()
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

        fun bind(content: String) {
            with(itemView) {

            }
        }
    }
}