package com.inu.cafeteria.feature.info

import android.view.ViewGroup
import com.inu.cafeteria.R
import com.inu.cafeteria.common.base.BaseAdapter
import com.inu.cafeteria.common.base.BaseViewHolder
import com.inu.cafeteria.common.extension.inflate
import com.inu.cafeteria.common.extension.partialBold
import com.inu.cafeteria.injection.ME
import com.inu.cafeteria.model.AuthorGroup
import kotlinx.android.synthetic.main.author_list_item.view.*

class AuthorsAdapter : BaseAdapter<AuthorGroup>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return BaseViewHolder(parent.inflate(R.layout.author_list_item))
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        val view = holder.containerView
        val item = getItem(position) ?: return

        with(view.phase) {
            text = context.getString(R.string.phase, item.phase)
        }

        with(view.authors) {
            val original = item.authors.joinToString(separator = "\n") { it.name + "-" + it.part }
            text = original
            setOnClickListener { text = original.partialBold(ME.name) }
        }
    }
}