package com.inu.cafeteria.common.base

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer

open class BaseViewHolder(view: View) : RecyclerView.ViewHolder(view), LayoutContainer {
    override val containerView: View = view
}
