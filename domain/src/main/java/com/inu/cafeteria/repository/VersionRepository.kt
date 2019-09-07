/**
 * Copyright (C) 2018-2019 INU Appcenter. All rights reserved.
 *
 * This file is part of INU Cafeteria.
 *
 * This work is licensed under the terms of the MIT license.
 * For a copy, see <https://opensource.org/licenses/MIT>.
 */

package com.inu.cafeteria.repository

import com.inu.cafeteria.model.scheme.Version

abstract class VersionRepository : Repository() {
    /**
     * Dismiss update popup of this version.
     */
    abstract fun dismissVersion(version: String)

    /**
     * Get last dismissed version.
     */
    abstract fun getDismissedVersion(): String?

    abstract fun getCurrentVersion(): String
    abstract fun getLatestVersion(callback: DataCallback<Version>)
}