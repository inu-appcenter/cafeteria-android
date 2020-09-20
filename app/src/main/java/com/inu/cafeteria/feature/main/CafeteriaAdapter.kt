package com.inu.cafeteria.feature.main

import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.inu.cafeteria.R
import kotlinx.android.synthetic.main.cafeteria.view.*

class CafeteriaAdapter : RecyclerView.Adapter<CafeteriaAdapter.CafeteriaViewHolder>() {

    private val menuPagePool = RecyclerView.RecycledViewPool()
    private val menuPool = RecyclerView.RecycledViewPool()

    var onClickMore: (CafeteriaView) -> Any? = {}

    var cafeteria: List<CafeteriaView> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CafeteriaViewHolder {
        return CafeteriaViewHolder(parent)
    }

    override fun onBindViewHolder(holder: CafeteriaViewHolder, position: Int) {
        holder.bind(cafeteria[position])
    }

    override fun getItemCount(): Int {
        return cafeteria.size
    }

    inner class CafeteriaViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        constructor(parent: ViewGroup) : this(LayoutInflater.from(parent.context).inflate(R.layout.cafeteria, parent, false))

        private val menuPageAdapter = MenuPageAdapter(menuPool)

        init {
            setChildRecyclerView()
        }

        private fun setChildRecyclerView() {
            with(itemView.menu_page_recycler) {
                adapter = menuPageAdapter

                setRecycledViewPool(menuPagePool)
                (layoutManager as LinearLayoutManager).recycleChildrenOnDetach = true

                PagerSnapHelper().attachToRecyclerView(this)
            }
        }

        fun bind(cafeteria: CafeteriaView) {
            with(itemView) {
                cafeteria_name.text = cafeteria.name

                more_button.setOnClickListener {
                    onClickMore(cafeteria)
                }
            }

            menuPageAdapter.wholeMenus = cafeteria.wholeMenus
        }
    }
}