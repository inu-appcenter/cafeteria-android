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
import com.inu.cafeteria.entities.PerfectReviewCondition
import timber.log.Timber
import java.util.*

class AppUsageRepositoryImpl(
    private val context: Context,
    private val db: SharedPreferenceWrapper
) : AppUsageRepository {

    override fun markReviewRequestShown(condition: PerfectReviewCondition) {
        db.putBoolean(condition.hasBeenAskedKey, true)
    }

    override fun markReviewChanceExposed(condition: PerfectReviewCondition) {
        db.putInt(condition.exposureCountKey, db.getInt(condition.exposureCountKey, 0) + 1)
    }

    override fun isThisPerfectTimeForReview(condition: PerfectReviewCondition): Boolean {
        val exposures = db.getInt(condition.exposureCountKey, 0)

        val hasBeenAsked = hasBeenAsked(condition)
        val hadEnoughTime = (getElapsedDaysFromInstall() >= condition.minimumDaysFromInstall)
        val hadEnoughExposures = (exposures >= condition.minimumPreExposure)

        return !hasBeenAsked && hadEnoughTime && hadEnoughExposures
    }

    private fun hasBeenAsked(condition: PerfectReviewCondition): Boolean {
        return db.getBoolean(condition.hasBeenAskedKey, false)
    }

    private fun getElapsedDaysFromInstall(): Long {
        val installTime = getFirstInstallTime() ?: return 0
        val now = Date().time
        val elapsedMillis = now - installTime

        return elapsedMillis / (60 * 60 * 24 * 1000/*a day*/)
    }

    private fun getFirstInstallTime(): Long? {
        return try {
            context.packageManager.getPackageInfo(Config.appId, 0).firstInstallTime
        } catch (e: Exception) {
            Timber.e("Could not get first install time: $e")
            null
        }
    }
}