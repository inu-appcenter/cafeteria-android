package com.inu.cafeteria.feature.main

import android.view.ViewGroup
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.inu.cafeteria.R
import com.inu.cafeteria.common.base.BaseAdapter
import com.inu.cafeteria.common.base.BaseViewHolder
import kotlinx.android.synthetic.main.cafeteria.view.*
import timber.log.Timber

class CafeteriaAdapter : BaseAdapter<CafeteriaView>() {

    private val menuPagePool = RecyclerView.RecycledViewPool()
    private val menuPool = RecyclerView.RecycledViewPool()

    var onClickMore: (CafeteriaView) -> Any? = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return CafeteriaViewHolder(parent)
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        (holder as CafeteriaViewHolder).bind(getItem(position))
    }

    inner class CafeteriaViewHolder(parent: ViewGroup) : BaseViewHolder(parent, R.layout.cafeteria) {

        private val menuPageAdapter = MenuPageAdapter(menuPool)

        init {
            Timber.d("Inflate Cafeteria!")
            setChildRecyclerView()
        }

        private fun setChildRecyclerView() {
            with(itemView.menu_page_recycler) {
                adapter = menuPageAdapter

                setRecycledViewPool(menuPagePool)

                PagerSnapHelper().attachToRecyclerView(this)
            }
        }

        fun bind(cafeteria: CafeteriaView?) {
            cafeteria ?: return

            with(itemView) {
                cafeteria_name.text = cafeteria.name

                more_button.setOnClickListener {
                    onClickMore(cafeteria)
                }
            }

            menuPageAdapter.data = cafeteria.wholeMenus
        }
    }
}