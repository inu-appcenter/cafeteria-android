package org.inu.cafeteria.common.base

import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import org.inu.cafeteria.R
import org.inu.cafeteria.common.extension.inImmediateTransaction

abstract class SingleFragmentActivity : BaseActivity() {

    abstract val fragment: Fragment
    open val layoutId: Int? = R.layout.single_fragment_activity

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        layoutId?.let { setContentView(it) }

        addFragment(savedInstanceState)
    }

    override fun onBackPressed() {
        (fragment as? BaseFragment)?.onBackPressed()
        super.onBackPressed()
    }

    private fun addFragment(savedInstanceState: Bundle?) =
        savedInstanceState ?:
        supportFragmentManager.inImmediateTransaction {
            add(R.id.fragment_container, fragment)
            this
        }
}