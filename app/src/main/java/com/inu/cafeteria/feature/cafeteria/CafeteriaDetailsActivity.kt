package com.inu.cafeteria.feature.cafeteria

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import com.inu.cafeteria.common.base.SingleFragmentActivity
import com.inu.cafeteria.model.json.Cafeteria

class CafeteriaDetailsActivity : SingleFragmentActivity() {
    // Go lazy or it will be null.
    override val fragment: Fragment by lazy {
        CafeteriaDetailFragment.forCafeteria(
            intent.getSerializableExtra(INTENT_EXTRA_PARAM_CAFETERIA) as Cafeteria
        )
    }

    companion object {
        fun callingIntent(context: Context, cafeteria: Cafeteria): Intent {
            return Intent(context, CafeteriaDetailsActivity::class.java).apply {
                putExtra(INTENT_EXTRA_PARAM_CAFETERIA, cafeteria)
            }
        }

        private const val INTENT_EXTRA_PARAM_CAFETERIA = "org.inu.INTENT_PARAM_CAFETERIA"
    }
}

