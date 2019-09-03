package org.inu.cafeteria.feature.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.login_fragment.view.*
import org.inu.cafeteria.R
import org.inu.cafeteria.common.base.BaseFragment

class LoginFragment : BaseFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // check autologin
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.login_fragment, container, false).apply {
            initializeView(this)
        }
    }

    private fun initializeView(view: View) {
        with(view.login) {

        }
    }

    private fun tryLoginWithIdAndPw(
        id: String,
        pw: String,
        onSuccess: () -> Unit,
        onFail: () -> Unit) {

    }

}