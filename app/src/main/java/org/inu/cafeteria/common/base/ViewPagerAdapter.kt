package org.inu.cafeteria.common.base

import android.content.Context
import android.util.SparseArray
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.core.util.set
import androidx.fragment.app.FragmentPagerAdapter
import androidx.recyclerview.widget.DiffUtil
import org.inu.cafeteria.common.extension.setVisible
import org.koin.core.inject

/**
 * Base pager adapter that use view for page item.
 */
abstract class ViewPagerAdapter : BasePagerAdapter() {

    protected val context: Context by inject()

    /**
     * Prevent calling super.
     * This method works for [FragmentPagerAdapter].
     */
    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }
}