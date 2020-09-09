/**
 * This file is part of INU Cafeteria.
 *
 * Copyright (C) 2020 INU Global App Center <potados99@gmail.com>
 *
 * INU Cafeteria is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * INU Cafeteria is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.inu.cafeteria.feature.cafeteria

import android.os.Bundle
import android.transition.ChangeBounds
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.inu.cafeteria.R
import com.inu.cafeteria.common.base.BaseFragment
import com.inu.cafeteria.common.extension.*
import com.inu.cafeteria.databinding.CafeteriaDetailsFragmentBinding
import com.inu.cafeteria.extension.onNull
import com.inu.cafeteria.model.FoodMenu
import com.inu.cafeteria.model.json.Cafeteria
import com.inu.cafeteria.repository.PrivateRepository
import kotlinx.android.synthetic.main.cafeteria_details_fragment.*
import kotlinx.android.synthetic.main.cafeteria_details_fragment.view.*
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

            // Set duration.
            // This should be done in the callee activity.
            it.window.sharedElementEnterTransition = ChangeBounds().apply {
                startDelay = 0
                duration = 200 /* TODO */

}
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
            // This is a first time this fragment is created.
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

                detailsViewModel
                    .cafeteria
                    ?.backgroundImagePath
                    ?.takeIf { it.isNotBlank() }
                    ?.also { loadFromUrl(privateRepo.getServerBaseUrl().addUrl(it)) }
                    .onNull { loadFromDrawable(R.drawable.no_img) }
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
     * Called when CafeteriaDetailFragment::loadFoodMenu is successfully done.
     * This method is called only when this fragment is first created.
     *
     * Nothing to do with data here.
     * View will be updated automatically.
     *
     * @param food the updated food menus per corners of this cafeteria.
     */

private fun foodMenuUpdated(food: FoodMenu?) {
        activity?.let { activity ->
            // This is the situation where the first data fetch took place.
            // We need to set the cafeteria image, and postpone the transition.
            with (cafeteria_image) {
                detailsViewModel
                    .cafeteria
                    ?.backgroundImagePath
                    ?.takeIf { it.isNotBlank() }
                    ?.also { loadUrlAndResumeEnterTransition(privateRepo.getServerBaseUrl().addUrl(it), activity) }
                    .onNull { loadDrawableAndResumeEnterTransition(R.drawable.no_img, activity) }
            }
        }

        // To suppress damn warning.
        food ?: return
    }

    /**
     * Invoked by parent activity, when swipe down gesture is detected.
     *
     */

fun onSwipeDown() {
        val expanded = viewDataBinding.appbar.height - viewDataBinding.appbar.bottom == 0
        if (expanded) {
            activity?.onBackPressed()
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