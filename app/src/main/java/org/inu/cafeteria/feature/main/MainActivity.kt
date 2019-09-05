package org.inu.cafeteria.feature.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.main_activity.*
import kotlinx.android.synthetic.main.drawer.*
import kotlinx.android.synthetic.main.toolbar.*
import org.inu.cafeteria.R
import org.inu.cafeteria.common.base.SingleFragmentActivity
import org.inu.cafeteria.common.extension.getViewModel
import org.inu.cafeteria.common.extension.defaultDataErrorHandle
import org.inu.cafeteria.common.extension.setSupportActionBar
import org.inu.cafeteria.databinding.MainActivityBinding
import org.inu.cafeteria.feature.cafeteria.CafeteriaListFragment

class MainActivity : SingleFragmentActivity() {
    override val fragment: Fragment = CafeteriaListFragment()
    override val layoutId: Int? = null // Will not inflate view through Activity.setContentView

    private lateinit var mainViewModel: MainViewModel
    private lateinit var viewDataBinding: MainActivityBinding
    private lateinit var toggle: ActionBarDrawerToggle

    private val drawerStartedOpen = {
        with(mainViewModel) {
            tryLoadingBarcode(
                onSuccess = {
                    barcode_image.setImageBitmap(it)
                    barcode_image.invalidate()
                },
                onFail = ::handleActivateBarcodeFailure,
                onNoBarcode = {
                    fail(R.string.fail_no_barcode)
                }
            )
        }
    }

    private val drawerClosed = {
        with(mainViewModel) {
            tryInvalidateBarcode(
                onFail = ::defaultDataErrorHandle,
                onNoBarcode = {
                    fail(R.string.fail_no_barcode)
                }
            )
        }
    }

    init {
        failables += this
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setViewModel()
        setViewDataBinding()
        setSupportActionBar(toolbar, title = false, upButton = false)
        setToggle()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)

        when (item.itemId) {
            R.id.menu_logout -> {
                mainViewModel.tryLogout(
                    onSuccess = {
                        mainViewModel.showLogin(this)
                    },
                    onFail = ::defaultDataErrorHandle,
                    onNoToken = { fail(R.string.fail_token_invalid, show = true) }
                )
            }
        }

        return true
    }

    private fun setViewModel() {
        mainViewModel = getViewModel()
        failables += mainViewModel.failables
    }

    private fun setViewDataBinding() {
        viewDataBinding = DataBindingUtil.setContentView(this, R.layout.main_activity)

        with(viewDataBinding) {
            lifecycleOwner = this@MainActivity
            mainViewModel = this@MainActivity.mainViewModel
        }
    }

    /**
     * Set drawer toggle.
     * Drawer callbacks are set here.
     */
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
                    drawerStartedOpen()
                }

                if (newState == DrawerLayout.STATE_IDLE &&
                    !root_layout.isDrawerOpen(GravityCompat.START)) {
                    drawerClosed()
                }
            }
        }

        root_layout.addDrawerListener(toggle)

        toggle.syncState()
    }


    companion object {
        fun callingIntent(context: Context) = Intent(context, MainActivity::class.java)
    }
}