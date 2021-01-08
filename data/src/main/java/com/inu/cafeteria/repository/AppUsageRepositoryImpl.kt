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

package com.inu.cafeteria.repository

import android.content.Context
import com.inu.cafeteria.config.Config
import com.inu.cafeteria.db.SharedPreferenceWrapper
import timber.log.Timber
import java.util.*

class AppUsageRepositoryImpl(
    private val context: Context,
    private val db: SharedPreferenceWrapper
) : AppUsageRepository {

    override fun getElapsedTimeFromInstall(): Long {
        val installTime = getFirstInstallTime() ?: return 0
        val now = Date().time

        return now - installTime // Millis
    }

    private fun getFirstInstallTime(): Long? {
        return try {
            context
                .packageManager
                .getPackageInfo(Config.appId, 0)
                .firstInstallTime
        } catch (e: Exception) {
            Timber.e("Could not get first install time: $e")
            null
        }
    }

    override fun hasUserLeftReview(): Boolean {
        return db.getBoolean(KEY_USER_LEFT_REVIEW, false)
    }

    override fun hasUserRefusedToReview(): Boolean {
        return db.getBoolean(KEY_USER_REFUSED_TO_REVIEW, false)
    }

    override fun markUserLeftReview() {
        db.putBoolean(KEY_USER_LEFT_REVIEW, true)
    }

    override fun markUserRefusedToReview() {
        db.putBoolean(KEY_USER_REFUSED_TO_REVIEW, true)
    }

    override fun isThisPerfectTimeForReview(): Boolean {
        val exposures = db.getInt(KEY_PERFECT_REVIEW_TIMING_EXPOSURES, 0)
        db.putInt(KEY_PERFECT_REVIEW_TIMING_EXPOSURES, exposures + 1)

        val userReviewed = hasUserLeftReview()
        val userRejected = hasUserRefusedToReview()
        val hadEnoughTime = getElapsedTimeFromInstall() > 60 * 60 * 24 * 14 * 1000 // 2 weeks
        val hadEnoughExposures = exposures >= 3

        return !userReviewed && !userRejected && hadEnoughTime && hadEnoughExposures
    }

    companion object {
        private const val KEY_USER_LEFT_REVIEW = "com.inu.cafeteria.user_left_review"
        private const val KEY_USER_REFUSED_TO_REVIEW = "com.inu.cafeteria.user_refused_to_review"
        private const val KEY_PERFECT_REVIEW_TIMING_EXPOSURES = "com.inu.cafeteria.perfect_review_timing_exposures"
    }
}