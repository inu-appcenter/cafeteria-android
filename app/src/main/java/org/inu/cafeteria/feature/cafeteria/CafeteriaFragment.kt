package org.inu.cafeteria.feature.cafeteria

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.cafeteria_fragment.view.*
import org.inu.cafeteria.R
import org.inu.cafeteria.common.base.BaseFragment
import org.inu.cafeteria.common.extension.getViewModel
import org.inu.cafeteria.common.widget.ZoomOutPageTransformer
import org.inu.cafeteria.databinding.CafeteriaFragmentBinding

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
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return CafeteriaFragmentBinding
            .inflate(inflater)
            .apply { lifecycleOwner = this@CafeteriaFragment }
            .apply { viewDataBinding = this }
            .apply { vm = cafeteriaViewModel }
            .apply { cafeteriaViewModel.loadAll(savedInstanceState == null) }
            .apply { initializeView(root) }
            .root
    }

    private fun initializeView(view: View) {
        with(view.cafeteria_viewpager) {
            setPageTransformer(false, ZoomOutPageTransformer(true))
            offscreenPageLimit = 5
        }

    }

}