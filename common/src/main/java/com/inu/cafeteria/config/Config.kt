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

package com.inu.cafeteria.config

import android.os.Build
import org.koin.core.KoinComponent
import org.koin.core.inject

/**
 * Global(through all modules) app config.
 * Part of them are came from Firebase Remote Config.
 */
object Config : KoinComponent {

    // Properties of com.inu.cafeteria.BuildConfig(app module)
    // are carried by BuildConfigHolder.
    private val appBuildConfigHolder: BuildConfigHolder by inject()

    // Aliases
    private fun string(key: String) = RemoteConfig.getString(key)
    private fun boolean(key: String) = RemoteConfig.getBoolean(key)

    /** Build settings */
    val deviceInfo = "${Build.MANUFACTURER} ${Build.MODEL}; Android ${Build.VERSION.RELEASE}"
    val version = appBuildConfigHolder.versionName
    val appId = appBuildConfigHolder.applicationId

    /** API endpoints */
    val baseUrl get() = when (appBuildConfigHolder.serverFlavor) {
        "emulator" -> "http://10.0.2.2:9999"
        "localserver" -> "http://10.0.1.10:9999"
        else -> string("url_api_base")
    }
    val manualPageUrl get() = string("url_service_manual")
    val faqPageUrl get() = string("url_faq")
    val feedbackUrl get() = string("url_beta_feedback")
    val termsAndConditionsUrl get() = string("url_terms_and_conditions")

    /** Feature parameters */
    val handleFinishedOrderAction get() = string("action_handle_finished_order")
    val showOrderTab get() = boolean("feature_order_tab_enabled")

    /** External sources */
    val kakaoPlusFriendLink get() = string("url_appcenter_kakao_friend")
    val uicoopPhoneNumber get() = string("tel_uicoop")
}