package com.inu.cafeteria.feature.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.inu.cafeteria.R
import kotlinx.android.synthetic.main.menu_page.view.*
import kotlin.math.ceil
import kotlin.math.min

class MenuPageAdapter(
    private val menuPool: RecyclerView.RecycledViewPool = RecyclerView.RecycledViewPool()
) : RecyclerView.Adapter<MenuPageAdapter.PropPageViewHolder>() {

    var wholeMenus: List<MenuView> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PropPageViewHolder {
        return PropPageViewHolder(parent)
    }

    override fun onBindViewHolder(holder: PropPageViewHolder, position: Int) {
        fun paginateProps(pageNumber: Int): List<MenuView> {
            val indexStart = pageNumber * stackSize
            val indexEnd = min(indexStart + stackSize - 1, wholeMenus.size - 1)

            return wholeMenus.slice(indexStart..indexEnd)
        }

        holder.bind(paginateProps(position))
    }

    override fun getItemCount(): Int {
        return ceil(wholeMenus.size.toDouble()/stackSize).toInt()
    }

    inner class PropPageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        constructor(parent: ViewGroup) : this(LayoutInflater.from(parent.context).inflate(R.layout.menu_page, parent, false))

        private val menuAdapter = MenuAdapter()

        init {
            setChildRecyclerView()
        }

        private fun setChildRecyclerView() {
            with(itemView.menu_recycler) {
                adapter = menuAdapter

                setRecycledViewPool(menuPool)
                (layoutManager as LinearLayoutManager).recycleChildrenOnDetach = true
            }
        }

        fun bind(pagedMenus: List<MenuView>) {
            menuAdapter.pagedMenus = pagedMenus
        }
    }

    companion object {
        private const val stackSize: Int = 3
    }
}