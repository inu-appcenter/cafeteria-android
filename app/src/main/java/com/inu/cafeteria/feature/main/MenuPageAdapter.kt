package com.inu.cafeteria.feature.main

import android.graphics.drawable.InsetDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.inu.cafeteria.R
import kotlinx.android.synthetic.main.menu_page.view.*
import timber.log.Timber
import kotlin.math.ceil
import kotlin.math.min


class MenuPageAdapter(
    private val menuPool: RecyclerView.RecycledViewPool = RecyclerView.RecycledViewPool()
) : RecyclerView.Adapter<MenuPageAdapter.MenuPageViewHolder>() {

    var wholeMenus: List<MenuView> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuPageViewHolder {
        return MenuPageViewHolder(parent)
    }

    override fun onBindViewHolder(holder: MenuPageViewHolder, position: Int) {
        fun paginateProps(pageNumber: Int): List<MenuView> {
            val indexStart = pageNumber * stackSize
            val indexEnd = min(indexStart + stackSize - 1, wholeMenus.size - 1)

            return wholeMenus.slice(indexStart..indexEnd)
        }

        holder.bind(paginateProps(position))
    }

    override fun getItemCount(): Int {
        return ceil(wholeMenus.size.toDouble() / stackSize).toInt()
    }

    inner class MenuPageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        constructor(parent: ViewGroup) : this(
            LayoutInflater.from(parent.context).inflate(
                R.layout.menu_page,
                parent,
                false
            )
        )

        private val menuAdapter = MenuAdapter()

        init {
            setChildRecyclerView()
            Timber.i("Inflate Menu Page!")
        }

        private fun setChildRecyclerView() {
            with(itemView.menu_recycler) {
                adapter = menuAdapter

                setRecycledViewPool(menuPool)

                // TODO: dirty. clean it.
                val divider = context.getDrawable(R.drawable.line_divider)
                val inset = resources.getDimensionPixelSize(R.dimen.left_margin_until_text)
                val insetDivider = InsetDrawable(divider, inset, 0, 0, 0)
                val itemDecoration = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
                itemDecoration.setDrawable(insetDivider)
                addItemDecoration(itemDecoration)
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