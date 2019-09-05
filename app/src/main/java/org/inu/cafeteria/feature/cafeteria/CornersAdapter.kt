package org.inu.cafeteria.feature.cafeteria

import android.view.LayoutInflater
import android.view.ViewGroup
import kotlinx.android.synthetic.main.food_list_item.view.*
import org.inu.cafeteria.R
import org.inu.cafeteria.common.base.BaseAdapter
import org.inu.cafeteria.common.base.BaseViewHolder
import org.inu.cafeteria.common.extension.inflate
import org.inu.cafeteria.model.FoodMenu

class CornersAdapter : BaseAdapter<FoodMenu.Corner>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val view = parent.inflate(R.layout.food_list_item)

        return BaseViewHolder(view)
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        val view = holder.containerView
        val item = getItem(position) ?: return

        with(view.corner_title) {
            text = item.title
        }

        with(view.menu) {
            text = item.menu.joinToString()
        }
    }
}