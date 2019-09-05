package org.inu.cafeteria.feature.cafeteria

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.cafeteria_details_fragment.*
import kotlinx.android.synthetic.main.cafeteria_details_fragment.view.*
import org.inu.cafeteria.common.base.BaseFragment
import org.inu.cafeteria.common.extension.*
import org.inu.cafeteria.databinding.CafeteriaDetailsFragmentBinding
import org.inu.cafeteria.model.FoodMenu
import org.inu.cafeteria.model.json.Cafeteria
import org.inu.cafeteria.repository.PrivateRepository
import org.koin.core.inject

class CafeteriaDetailFragment : BaseFragment() {

    private val privateRepo: PrivateRepository by inject()
    private val animator: CafeteriaDetailsAnimator by inject()

    private lateinit var detailsViewModel: CafeteriaDetailsViewModel
    private lateinit var viewDataBinding: CafeteriaDetailsFragmentBinding

    private val cornersAdapter = CornersAdapter()

    init {
        failables += this
        failables += privateRepo
        failables += animator
        failables += cornersAdapter
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activity?.let {
            // Delay transition until we finished loading the image.
            animator.postponeEnterTransition(it)
        }

        detailsViewModel = getViewModel {
            startWithCafeteria(arguments!![PARAM_CAFETERIA] as? Cafeteria)
            observe(food, ::foodMenuUpdated)
        }
        failables += detailsViewModel.failables
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return CafeteriaDetailsFragmentBinding
            .inflate(inflater)
            .apply { lifecycleOwner = this@CafeteriaDetailFragment }
            .apply { viewDataBinding = this }
            .apply { vm = detailsViewModel }
            .apply { initializeView(root, firstTimeCreated(savedInstanceState)) }
            .root
    }

    /**
     * Initialize view.
     *
     * If it is first created, cancel the transition.
     */
    private fun initializeView(view: View, firstTimeCreated: Boolean) {
        if (firstTimeCreated) {
            // This is a first time this fragment is create.
            // It is obvious that user just clicked the detail
            // and jumped into this fragment.
            // So load the food and then resume transition.
            detailsViewModel.loadFoodMenu()
        } else {
            // Do not show transition animation when the view is recreated.
            // Transition should only be shown at the first view creation.
            with(view.cafeteria_image) {

                // This is not a situation that a user just clicked
                // a content, so the transition will look odd.
                //
                // Remove any transition on the cafeteria image.
                // And then load image again because the view has been destroyed.
                animator.cancelTransition(this)

                loadFromUrl(
                    privateRepo.getServerBaseUrl().addUrl(detailsViewModel.cafeteria?.backgroundImagePath)
                )
            }
        }

        with(view.corners_list) {
            cornersAdapter.emptyView = view.empty_view
            adapter = cornersAdapter
        }

        with(view.cancel_button) {
            setOnClickListener {
                activity?.onBackPressed()
            }
        }
    }

    /**
     * Called when [CafeteriaDetailFragment.loadFoodMenu] is successfully done.
     * This method is called only when this fragment is first created.
     *
     * Nothing to do with data here.
     * View will be updated automatically.
     *
     * @param food the updated food menus per corners of this cafeteria.
     */
    private fun foodMenuUpdated(food: FoodMenu?) {
        activity?.let {
            // This is the situation where the first data fetch took place.
            // We need to set the cafeteria image, and postpone the transition.
            cafeteria_image.loadUrlAndResumeEnterTransition(
                privateRepo.getServerBaseUrl().addUrl(detailsViewModel.cafeteria?.backgroundImagePath), it
            )
        }
    }


    companion object {
        fun forCafeteria(cafeteria: Cafeteria): CafeteriaDetailFragment {
            return CafeteriaDetailFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(PARAM_CAFETERIA, cafeteria)
                }
            }
        }

        private const val PARAM_CAFETERIA = "param_cafeteria"
    }
}