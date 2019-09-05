package org.inu.cafeteria.feature.cafeteria

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import org.inu.cafeteria.common.base.SingleFragmentActivity

class CafeteriaDetailActivity : SingleFragmentActivity() {
    override val fragment: Fragment = CafeteriaDetailFragment.forCafeteria(
        intent.getIntExtra(INTENT_EXTRA_PARAM_CAFETERIA, -1)
    )

    companion object {
        fun callingIntent(context: Context, cafeteriaNumber: Int): Intent {
            return Intent(context, CafeteriaDetailActivity::class.java).apply {
                putExtra(INTENT_EXTRA_PARAM_CAFETERIA, cafeteriaNumber)
            }
        }

        private const val INTENT_EXTRA_PARAM_CAFETERIA = "org.inu.INTENT_PARAM_CAFETERIA"
    }
}