/**
 * Copyright (C) 2018-2019 INU Appcenter. All rights reserved.
 *
 * This file is part of INU Cafeteria.
 *
 * This work is licensed under the terms of the MIT license.
 * For a copy, see <https://opensource.org/licenses/MIT>.
 */

package com.inu.cafeteria.feature.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.inu.cafeteria.R
import com.inu.cafeteria.common.base.SingleFragmentActivity
import com.inu.cafeteria.common.extension.defaultDataErrorHandle
import com.inu.cafeteria.common.extension.getViewModel
import com.inu.cafeteria.common.extension.isVisible
import com.inu.cafeteria.common.extension.setSupportActionBar
import com.inu.cafeteria.common.widget.ThemedDialog
import com.inu.cafeteria.feature.cafeteria.CafeteriaListFragment
import com.inu.cafeteria.repository.LoginRepository
import kotlinx.android.synthetic.main.main_activity.*
import kotlinx.android.synthetic.main.toolbar.*
import org.koin.android.ext.android.inject

/**
 * Manage student data here.
 */
class MainActivity : SingleFragmentActivity() {

    override val fragment: Fragment = CafeteriaListFragment()
    override val layoutId: Int? = null

    private val loginRepo: LoginRepository by inject()

    private lateinit var mainViewModel: MainViewModel

    private val logout = {
        mainViewModel.tryLogout(
            onSuccess = {
                mainViewModel.showLogin(this)
            },
            onFail = ::defaultDataErrorHandle,
            onNoToken = { fail(R.string.fail_token_invalid, show = true) }
        )

        // Remove all user data even if the logout got failed.
        mainViewModel.removeUserData()
    }

    init {
        failables += this
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.main_activity)
        setViewModel()
        setSupportActionBar(toolbar, title = false, upButton = false)

        initializeView()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        MenuInflater(this).inflate(R.menu.menu_main, menu)
        menu?.let(::initializeMenu)

        return true // Display the menu.
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)

        when (item.itemId) {
            R.id.menu_app_info -> {
                mainViewModel.showInfo()
            }
            R.id.menu_logout -> {
                ThemedDialog(this)
                    .withTitle(R.string.title_logout)
                    .withMessage(R.string.dialog_ask_logout)
                    .withPositiveButton(R.string.button_confirm) { logout() }
                    .withNegativeButton(R.string.button_cancel)
                    .show()
            }
            R.id.menu_login -> {
                mainViewModel.showLogin(this)
            }
        }

        return true
    }

    private fun setViewModel() {
        mainViewModel = getViewModel()
        failables += mainViewModel.failables
    }

    private fun initializeView() {
        with(barcode_button) {
            isVisible = loginRepo.isLoggedIn()
            setOnClickListener {
                mainViewModel.showBarcode()
            }
        }
    }

    private fun initializeMenu(menu: Menu) {
        menu.findItem(R.id.menu_logout)?.isVisible = loginRepo.isLoggedIn()
        menu.findItem(R.id.menu_login)?.isVisible = loginRepo.isLoggedIn().not()
    }

    companion object {
        fun callingIntent(context: Context) = Intent(context, MainActivity::class.java)
    }
}