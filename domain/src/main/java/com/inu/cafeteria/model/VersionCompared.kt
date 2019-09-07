/**
 * Copyright (C) 2018-2019 INU Appcenter. All rights reserved.
 *
 * This file is part of INU Cafeteria.
 *
 * This work is licensed under the terms of the MIT license.
 * For a copy, see <https://opensource.org/licenses/MIT>.
 */

package com.inu.cafeteria.model

/**
 * A class having a current version and a latest version.
 * It is useful when we want to get and compare versions in one place.
 */
data class VersionCompared(val currentVersion: String, val latestVersion: String) {
    fun needUpdate(): Boolean {
        return !isUpToDate()
    }
    fun isUpToDate(): Boolean {
        return latestVersion <= currentVersion
    }
}