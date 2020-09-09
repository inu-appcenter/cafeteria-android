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

package com.inu.cafeteria.injection

import com.inu.cafeteria.model.OpenSourceSoftware

/**
 * We have no OSS DB!
 * But we need to add open source notices!
 * So we do it by hand!
 */

/**
 * License of this app.
 * This app is open source and we NEED to add license.
 */

val thisAppLicense = OpenSourceSoftware(
    name = "INU Cafeteria",
    contact = "inuappcenter@gmail.com",
    copyright = "Copyright 2018-2019 INU Appcenter.",
    devWebSite = "https://inu-appcenter.firebaseapp.com",
    licenseName = "MIT License",
    licenseReference = "https://opensource.org/licenses/MIT",
    sourceCodeReference = "https://github.com/inu-appcenter/INU_CafeTeria_android"
)

/**
 * Open source software used in this app.
 */

val openSourceLicenses = listOf(

    OpenSourceSoftware(
        name = "Glide",
        copyright = "Copyright 2014 Google, Inc. All rights reserved.",
        licenseName = "License for third_party/disklrucache:\n" +
                "Copyright 2012 Jake Wharton.\n" +
                "Copyright 2011 The Android Open Source Project.\n" +
                "License for third_party/gif_decoder:\n" +
                "Copyright (c) 2013 Xcellent Creations, Inc.\n" +
                "License for third_party/gif_encoder/NeuQuant.java\n" +
                "Copyright (c) 1994 Anthony Dekker.\n" +
                "BSD, part MIT and Apache 2.0 License",
        licenseReference = "https://raw.githubusercontent.com/bumptech/glide/master/LICENSE",
        sourceCodeReference = "https://github.com/bumptech/glide"
    ),
    OpenSourceSoftware(
        name = "Gson",
        copyright = "Copyright 2008 Google Inc.",
        licenseName = "Apache License 2.0",
        licenseReference = "http://www.apache.org/licenses/LICENSE-2.0",
        sourceCodeReference = "https://github.com/google/gson"
    ),
    OpenSourceSoftware(
        name = "Jsoup",
        copyright = "Copyright 2009 - 2019 Jonathan Hedley (jonathan@hedley.net).",
        licenseName = "MIT License",
        licenseReference = "https://opensource.org/licenses/MIT",
        sourceCodeReference = "https://github.com/jhy/jsoup/"
    ),
    OpenSourceSoftware(
        name = "Koin",
        copyright = "Copyright 2017-2018 the original author or authors.",
        licenseName = "Apache License 2.0",
        licenseReference = "http://www.apache.org/licenses/LICENSE-2.0",
        sourceCodeReference = "https://github.com/InsertKoinIO/koin"
    ),
    OpenSourceSoftware(
        name = "PersistentCookieJar",
        copyright = "Copyright 2016 Francisco José Montiel Navarro.",
        licenseName = "Apache License 2.0",
        licenseReference = "http://www.apache.org/licenses/LICENSE-2.0",
        sourceCodeReference = "https://github.com/franmontiel/PersistentCookieJar"
    ),
    OpenSourceSoftware(
        name = "Retrofit2",
        copyright = "Copyright 2013 Square, Inc.",
        licenseName = "Apache License 2.0",
        licenseReference = "http://www.apache.org/licenses/LICENSE-2.0",
        sourceCodeReference = "https://github.com/square/retrofit"
    ),
    OpenSourceSoftware(
        name = "Timber",
        copyright = "Copyright 2013 Jake Wharton.",
        licenseName = "Apache License 2.0",
        licenseReference = "http://www.apache.org/licenses/LICENSE-2.0",
        sourceCodeReference = "https://github.com/JakeWharton/timber"
    ),
    OpenSourceSoftware(
        name = "zxing",
        copyright = "Copyright 2010 ZXing authors.",
        licenseName = "Apache License 2.0",
        licenseReference = "http://www.apache.org/licenses/LICENSE-2.0",
        sourceCodeReference = "https://github.com/zxing/zxing"
    )
)
