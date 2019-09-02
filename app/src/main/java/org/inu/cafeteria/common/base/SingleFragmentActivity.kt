package org.inu.cafeteria.common.base

import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import org.inu.cafeteria.R
import org.inu.cafeteria.common.extension.inImmediateTransaction

abstract class SingleFragmentActivity : BaseActivity() {

    abstract val fragment: Fragment
    open val layoutId: Int = R.layout.single_fragment_activity
    open val toolbarId: Int? = null

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutId)

        addFragment(savedInstanceState)

        toolbarId?.let {
            setSupportActionBar(findViewById(it))
        }
    }

    private fun addFragment(savedInstanceState: Bundle?) =
        savedInstanceState ?:
        supportFragmentManager.inImmediateTransaction {
            add(R.id.fragment_container, fragment)
            this
        }
}