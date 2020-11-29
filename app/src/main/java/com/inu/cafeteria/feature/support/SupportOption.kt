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

package com.inu.cafeteria.feature.support

import com.inu.cafeteria.R

data class SupportOption(
    val title: Int,
    val navigateTo: Int,
    val somethingNew: Boolean = false,
) {
    companion object {
        // Logged in + have unread answers
        val availableSupportOptionsForThoseHaveNotification = listOf(
            SupportOption(R.string.title_notice, R.id.action_support_notice),
            SupportOption(R.string.title_service_manual, R.id.action_support_manual),
            SupportOption(R.string.title_faq, R.id.action_support_faq),
            SupportOption(R.string.title_ask, R.id.action_support_ask),
            SupportOption(R.string.title_questions, R.id.action_support_questions, true),
        )

        // Logged in + no unread answers
        val availableSupportOptionsForThoseLoggedIn = listOf(
            SupportOption(R.string.title_notice, R.id.action_support_notice),
            SupportOption(R.string.title_service_manual, R.id.action_support_manual),
            SupportOption(R.string.title_faq, R.id.action_support_faq),
            SupportOption(R.string.title_ask, R.id.action_support_ask),
            SupportOption(R.string.title_questions, R.id.action_support_questions),
        )

        // Not logged in + have unread answers
        val availableSupportOptionsForThoseNotLoggedIn = listOf(
            SupportOption(R.string.title_notice, R.id.action_support_notice),
            SupportOption(R.string.title_service_manual, R.id.action_support_manual),
            SupportOption(R.string.title_faq, R.id.action_support_faq),
        )
    }
}