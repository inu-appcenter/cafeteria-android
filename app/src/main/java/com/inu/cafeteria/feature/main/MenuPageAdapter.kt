package com.inu.cafeteria.feature.main

import android.graphics.drawable.InsetDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.inu.cafeteria.R
import com.inu.cafeteria.common.base.BaseAdapter
import com.inu.cafeteria.common.base.BaseViewHolder
import kotlinx.android.synthetic.main.menu_page.view.*
import timber.log.Timber
import kotlin.math.ceil
import kotlin.math.min


class MenuPageAdapter(
    private val menuPool: RecyclerView.RecycledViewPool = RecyclerView.RecycledViewPool()
) : BaseAdapter<MenuView>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return MenuPageViewHolder(parent)
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        fun paginateProps(pageNumber: Int): List<MenuView> {
            val indexStart = pageNumber * stackSize
            val indexEnd = min(indexStart + stackSize - 1, data.size - 1)

            return data.slice(indexStart..indexEnd)
        }

        (holder as MenuPageViewHolder).bind(paginateProps(position))
    }

    override fun getItemCount(): Int {
        return ceil(data.size.toDouble() / stackSize).toInt()
    }

    inner class MenuPageViewHolder(parent: ViewGroup) : BaseViewHolder(parent, R.layout.menu_page) {

        private val menuAdapter = MenuAdapter()

        init {
            Timber.d("Inflate Menu Page!")
            setChildRecyclerView()
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
            menuAdapter.data = pagedMenus
        }
    }

    companion object {
        private const val stackSize: Int = 3
    }
}