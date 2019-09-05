package org.inu.cafeteria.feature.cafeteria

import android.os.Bundle
import org.inu.cafeteria.common.base.BaseFragment

class CafeteriaDetailFragment : BaseFragment() {

    companion object {
        fun forCafeteria(cafeteriaNumber: Int): CafeteriaDetailFragment {
            return CafeteriaDetailFragment().apply {
                arguments = Bundle().apply {
                    putInt(PARAM_CAFETERIA, cafeteriaNumber)
                }
            }
        }

        private const val PARAM_CAFETERIA = "param_cafeteria"
    }
}