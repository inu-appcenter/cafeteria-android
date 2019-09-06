package org.inu.cafeteria.feature.cafeteria

import android.view.ViewGroup
import kotlinx.android.synthetic.main.corner_list_item.view.*
import org.inu.cafeteria.R
import org.inu.cafeteria.common.base.BaseAdapter
import org.inu.cafeteria.common.base.BaseViewHolder
import org.inu.cafeteria.common.base.FooterAdapter
import org.inu.cafeteria.common.extension.inflate
import org.inu.cafeteria.common.extension.isVisible
import org.inu.cafeteria.model.FoodMenu

class CornersAdapter : FooterAdapter<FoodMenu.Corner>() {

    override val footerLayoutId: Int = R.layout.cafeteria_list_footer

    override fun onCreateContentViewHolder(parent: ViewGroup): BaseViewHolder {
        val view = parent.inflate(R.layout.corner_list_item)

        return BaseViewHolder(view)
    }

    override fun onBindContentViewHolder(holder: BaseViewHolder, position: Int) {
        val view = holder.containerView
        val item = getItem(position) ?: return

        with(view.corner_title) {
            text = item.title
        }

        with(view.menu) {
            item.menu.joinToString(" ").also {
                if (it.isBlank()) {
                    text = context.getString(R.string.desc_no_data)
                    alpha = 0.6f
                } else {
                    text = it
                    alpha = 1.0f
                }
            }
        }
    }

    override fun onBindFooterViewHolder(holder: BaseViewHolder) {
        // Nothing to do with footer view.
        // The text is declared in xml.
    }
}