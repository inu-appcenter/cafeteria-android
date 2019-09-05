package org.inu.cafeteria.feature.cafeteria

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.cafeteria_list_fragment.view.*
import kotlinx.android.synthetic.main.cafeteria_list_item.view.*
import org.inu.cafeteria.common.Navigator
import org.inu.cafeteria.common.base.BaseFragment
import org.inu.cafeteria.common.extension.baseActivity
import org.inu.cafeteria.common.extension.getViewModel
import org.inu.cafeteria.databinding.CafeteriaListFragmentBinding
import org.koin.core.inject
import timber.log.Timber

class CafeteriaListFragment : BaseFragment() {

    private val navigator: Navigator by inject()

    private val cafeteriaAdapter = CafeteriaAdapter()

    private lateinit var cafeteriaListViewModel: CafeteriaListViewModel
    private lateinit var viewDataBinding: CafeteriaListFragmentBinding

    init {
        failables += this
        failables += cafeteriaAdapter
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        cafeteriaListViewModel = getViewModel()
        failables += cafeteriaListViewModel.failables
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return CafeteriaListFragmentBinding
            .inflate(inflater)
            .apply { lifecycleOwner = this@CafeteriaListFragment }
            .apply { viewDataBinding = this }
            .apply { vm = cafeteriaListViewModel }
            .apply { initializeView(root) }
            .apply { cafeteriaListViewModel.loadAll(firstTimeCreated(savedInstanceState)) }
            .root
    }

    private fun initializeView(view: View) {
        with(view.cafeteria_list) {
            cafeteriaAdapter.clickListener = { itemView, cafeteria ->
                navigator.showCafeteriaDetail(activity!!, cafeteria, itemView.cafeteria_image)
            }
            adapter = cafeteriaAdapter
        }


        Timber.i("CafeteriaListFragment initialized.")
    }
}