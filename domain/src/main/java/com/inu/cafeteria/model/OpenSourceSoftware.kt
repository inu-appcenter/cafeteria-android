/**
 * Copyright (C) 2018-2019 INU Appcenter. All rights reserved.
 *
 * This file is part of INU Cafeteria.
 *
 * This work is licensed under the terms of the MIT license.
 * For a copy, see <https://opensource.org/licenses/MIT>.
 */

package com.inu.cafeteria.model

data class OpenSourceSoftware(
    val name: String,
    val contact: String = "",
    val copyright: String,
    val devWebSite: String = "",
    val licenseName: String,
    val licenseReference: String,
    val sourceCodeReference: String
)