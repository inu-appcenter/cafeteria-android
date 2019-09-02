package org.inu.cafeteria.feature.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.cafeteria_activity.*
import kotlinx.android.synthetic.main.drawer.*
import kotlinx.android.synthetic.main.toolbar.*
import org.inu.cafeteria.R
import org.inu.cafeteria.common.base.SingleFragmentActivity
import org.inu.cafeteria.common.extension.getViewModel
import org.inu.cafeteria.common.extension.isVisible
import org.inu.cafeteria.common.extension.setSupportActionBar
import org.inu.cafeteria.databinding.CafeteriaActivityBinding
import org.inu.cafeteria.model.BarcodeState
import timber.log.Timber

class CafeteriaActivity : SingleFragmentActivity() {
    override val fragment: Fragment = CafeteriaFragment()
    override val layoutId: Int? = null // Will not inflate view through Activity.setContentView

    private lateinit var drawerViewModel: DrawerViewModel
    private lateinit var viewDataBinding: CafeteriaActivityBinding
    private lateinit var toggle: ActionBarDrawerToggle

    private val onDrawerOpen = {
        drawerViewModel.barcodeState.value = BarcodeState(isAvailable = true, isLoading = true, isNetworkDown = false)

        Handler().postDelayed({
            drawerViewModel.barcodeState.value = BarcodeState(isAvailable = true, isLoading = false, isNetworkDown = false)
        }, 500)

        Timber.i("Drawer opened.")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setViewModel()
        setViewDataBinding()
        setSupportActionBar(toolbar, title = false, upButton = false)
        setToggle()

    }

    private fun setViewModel() {
        drawerViewModel = getViewModel()
    }

    private fun setViewDataBinding() {
        viewDataBinding = DataBindingUtil.setContentView(this, R.layout.cafeteria_activity)

        with(viewDataBinding) {
            lifecycleOwner = this@CafeteriaActivity
            drawerViewModel = this@CafeteriaActivity.drawerViewModel
        }
    }

    private fun setToggle() {
        toggle = object : ActionBarDrawerToggle(
            this,
            root_layout,
            toolbar,
            R.string.desc_close_drawer,
            R.string.desc_open_drawer
        ) {
            override fun onDrawerStateChanged(newState: Int) {
                super.onDrawerStateChanged(newState)

                if (newState == DrawerLayout.STATE_SETTLING &&
                    !root_layout.isDrawerOpen(GravityCompat.START)) {
                    onDrawerOpen()
                }
            }
        }

        root_layout.addDrawerListener(toggle)

        toggle.syncState()
    }

    companion object {
        fun callingIntent(context: Context) = Intent(context, CafeteriaActivity::class.java)
    }
}