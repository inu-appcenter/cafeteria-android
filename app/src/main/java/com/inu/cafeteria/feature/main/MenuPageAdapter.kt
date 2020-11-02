package com.inu.cafeteria.feature.main

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.inu.cafeteria.R
import com.inu.cafeteria.common.base.BaseViewHolder
import com.inu.cafeteria.common.base.DefaultAdapter
import com.inu.cafeteria.common.extension.setLeftInsetDivider
import kotlinx.android.synthetic.main.menu_page.view.*
import timber.log.Timber
import kotlin.math.ceil
import kotlin.math.min

class MenuPageAdapter(
    private val menuPool: RecyclerView.RecycledViewPool = RecyclerView.RecycledViewPool(),
    private val pageSize: Int = DEFAULT_PAGE_SIZE
) : DefaultAdapter<MenuView>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return MenuPageViewHolder(parent)
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        fun paginateProps(pageNumber: Int): List<MenuView> {
            val indexStart = pageNumber * pageSize
            val indexEnd = min(indexStart + pageSize - 1, data.size - 1)

            return data.slice(indexStart..indexEnd)
        }

        (holder as MenuPageViewHolder).bind(
            if (pageSize == 0) data else paginateProps(position)
        )
    }

    override fun getItemCount(): Int {
        return if (pageSize == 0) 1 else ceil(data.size.toDouble() / pageSize).toInt()
    }

    inner class MenuPageViewHolder(parent: ViewGroup) : BaseViewHolder(parent, R.layout.menu_page) {

        private val menuAdapter = MenuAdapter()

        init {
            Timber.d("Inflate Menu Page view holder!")
            setChildRecyclerView()
        }

        private fun setChildRecyclerView() {
            with(itemView.menu_recycler) {
                adapter = menuAdapter

                setRecycledViewPool(menuPool)

                setLeftInsetDivider(R.drawable.line_divider, R.dimen.menu_left_margin_until_text, pageSize - 1)
            }
        }

        fun bind(pagedMenus: List<MenuView>) {
            menuAdapter.data = pagedMenus
        }
    }

    companion object {
        private const val DEFAULT_PAGE_SIZE: Int = 2
    }
}