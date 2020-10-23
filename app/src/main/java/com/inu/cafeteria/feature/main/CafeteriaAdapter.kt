package com.inu.cafeteria.feature.main

import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.inu.cafeteria.R
import com.inu.cafeteria.common.base.PositionRetainingAdapter
import com.inu.cafeteria.common.base.PositionRetainingViewHolder
import com.inu.cafeteria.common.extension.onScrollStateChange
import kotlinx.android.synthetic.main.cafeteria.view.*
import kotlinx.android.synthetic.main.empty_cafeteria_view.view.*
import timber.log.Timber

class CafeteriaAdapter : PositionRetainingAdapter<CafeteriaView>() {

    private val menuPagePool = RecyclerView.RecycledViewPool()
    private val menuPool = RecyclerView.RecycledViewPool()

    var onClickMore: (CafeteriaView) -> Any? = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PositionRetainingViewHolder {
        return CafeteriaViewHolder(parent)
    }

    override fun onBindViewHolder(holder: PositionRetainingViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)

        with(holder as CafeteriaViewHolder) {
            bind(getItem(position))
        }
    }

    inner class CafeteriaViewHolder(parent: ViewGroup) : PositionRetainingViewHolder(parent, R.layout.cafeteria) {

        override val layoutManager: LinearLayoutManager? = view.menu_page_recycler?.layoutManager as? LinearLayoutManager

        private val menuPageAdapter = MenuPageAdapter(menuPool)

        init {
            Timber.d("Inflate Cafeteria view holder!")

            setChildRecyclerView()
        }

        private fun setChildRecyclerView() {
            with(view.menu_page_recycler) {
                onScrollStateChange { saveViewHolderPosition(this@CafeteriaViewHolder) }

                adapter = menuPageAdapter.apply {
                    emptyView = itemView.empty_cafeteria_view
                }

                setRecycledViewPool(menuPagePool)

                PagerSnapHelper().attachToRecyclerView(this)
            }
        }

        fun bind(cafeteria: CafeteriaView?) {
            cafeteria ?: return

            with(view) {
                cafeteria_name.text = cafeteria.name

                more_button.setOnClickListener {
                    onClickMore(cafeteria)
                }
            }

            menuPageAdapter.data = cafeteria.wholeMenus
        }
    }
}