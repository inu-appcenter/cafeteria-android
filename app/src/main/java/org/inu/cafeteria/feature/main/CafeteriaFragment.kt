package org.inu.cafeteria.feature.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.inu.cafeteria.R
import org.inu.cafeteria.common.base.BaseFragment
import org.inu.cafeteria.common.extension.getViewModel
import org.inu.cafeteria.databinding.CafeteriaFragmentBinding
import org.inu.cafeteria.usecase.GetCafeteria
import org.koin.core.inject
import timber.log.Timber

class CafeteriaFragment : BaseFragment() {
    override val optionMenuId: Int? = R.menu.menu_main

    private lateinit var viewDataBinding: CafeteriaFragmentBinding
    private lateinit var cafeteriaViewModel: CafeteriaViewModel

    init {
        failables += this
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        cafeteriaViewModel = getViewModel()
        failables += cafeteriaViewModel.failables


        // TODO: TEST
        val getCafeteria: GetCafeteria by inject()

        getCafeteria(Unit) {
            it.onSuccess {
                it.forEach {
                    Timber.i("${it.key} : ${it.name}")
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return CafeteriaFragmentBinding
            .inflate(inflater)
            .apply { vm = cafeteriaViewModel }
            .apply { lifecycleOwner = this@CafeteriaFragment }
            .apply { viewDataBinding = this }
            .apply { initializeView(root) }
            .root
    }

    fun initializeView(view: View) {

    }


}