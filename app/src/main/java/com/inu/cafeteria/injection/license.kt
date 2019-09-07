/**
 * Copyright (C) 2018-2019 INU Appcenter. All rights reserved.
 *
 * This file is part of INU Cafeteria.
 *
 * This work is licensed under the terms of the MIT license.
 * For a copy, see <https://opensource.org/licenses/MIT>.
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
