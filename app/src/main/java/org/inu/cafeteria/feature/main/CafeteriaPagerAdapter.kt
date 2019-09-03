package org.inu.cafeteria.feature.main

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import org.inu.cafeteria.common.base.ViewPagerAdapter
import org.koin.core.inject

class CafeteriaPagerAdapter : ViewPagerAdapter() {

    private val context: Context by inject()




    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        return super.instantiateItem(container, position)
    }

    override fun getCount(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}