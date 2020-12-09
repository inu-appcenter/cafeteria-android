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

import android.content.Context
import android.net.ConnectivityManager
import android.os.Build
import com.inu.cafeteria.BuildConfig
import com.inu.cafeteria.GlobalConfig
import com.inu.cafeteria.common.EventHub
import com.inu.cafeteria.common.navigation.Navigator
import com.inu.cafeteria.db.SharedPreferenceWrapper
import com.inu.cafeteria.feature.main.LifecycleEventHandler
import com.inu.cafeteria.feature.main.LifecycleEventHandlerImplBeta
import com.inu.cafeteria.feature.main.LifecycleEventHandlerImplFinal
import com.inu.cafeteria.repository.*
import com.inu.cafeteria.service.AccountService
import com.inu.cafeteria.usecase.*
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val myModules = module {

    /*****************************
     * Global config
     *****************************/

    single {
        GlobalConfig(
            baseUrl = when (BuildConfig.FLAVOR_server) {
                "localserver" -> "http://10.0.2.2:9999" // 127.0.0.1 of host. For emulator.
                "productionserver" -> "https://api.inu-cafeteria.app"
                else -> "https://api.inu-cafeteria.app"
            },
            serviceManualPagePath = "/res/pages/manual/index.html",
            faqPagePath = "/res/pages/faq/index.html",
            deviceInfo = "${Build.MANUFACTURER} ${Build.MODEL}; Android ${Build.VERSION.RELEASE}" ,
            version = BuildConfig.VERSION_NAME,
            appId = BuildConfig.APPLICATION_ID,
            kakaoPlusFriendLink = "kakaoplus://plusfriend/home/_xgxaSLd",
            uicoopPhoneNumber = "0328354720",
            feedbackUrl = "https://beta.inu-cafeteria.app/feedback"
        )
    }


    /*****************************
     * General
     *****************************/

    /** Navigator */
    single {
        Navigator(
            context = get(),
            globalConfig = get()
        )
    }

    /** Network Service */
    single {
        RetrofitFactory.createCafeteriaNetworkService(
            context = get(),
            baseUrl = get<GlobalConfig>().baseUrl
        )
    }

    /** Live event hub */
    single {
        EventHub()
    }

    /** DB */
    single {
        SharedPreferenceWrapper(get())
    }

    /** Main activity event handler */
    single {
        when (BuildConfig.FLAVOR_stage) {
            "betatest" -> LifecycleEventHandlerImplBeta(context = get())
            "finalstage" -> LifecycleEventHandlerImplFinal()
            else -> LifecycleEventHandlerImplFinal()
        } as LifecycleEventHandler
    }


    /*****************************
     * Service
     *****************************/

    /** Account service */
    single {
        AccountService(
            accountRepo = get()
        )
    }


    /*****************************
     * Repository
     *****************************/

    /** Cafeteria repository */
    single {
        CafeteriaRepositoryImpl(
            networkService = get(),
            db = get()
        ) as CafeteriaRepository
    }

    /** Account repository */
    single {
        AccountRepositoryImpl(
            networkService = get(),
            db = get()
        ) as AccountRepository
    }

    /** Device status repository */
    single {
        DeviceStatusRepositoryImpl(
            manager = androidContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        ) as DeviceStatusRepository
    }

    /** Notice repository */
    single {
        NoticeRepositoryImpl(
            networkService = get(),
            db = get()
        ) as NoticeRepository
    }

    /** Version repository */
    single {
        VersionRepositoryImpl(
            networkService = get()
        ) as VersionRepository
    }

    /** Interaction repository */
    single {
        InteractionRepositoryImpl(
            networkService = get(),
            globalConfig = get()
        ) as InteractionRepository
    }

    /*****************************
     * Use Case
     *****************************/

    /** Activate barcode */
    single {
        ActivateBarcode(
            accountService = get()
        )
    }

    /** Create barcode */
    single {
        CreateBarcode()
    }

    /** Get cafeteria */
    single {
        GetCafeteria(
            cafeteriaRepo = get()
        )
    }

    /** Get cafeteria only */
    single {
        GetCafeteriaOnly(
            cafeteriaRepo = get()
        )
    }

    /** Get cafeteria order */
    single {
        GetCafeteriaOrder(
            cafeteriaRepo = get()
        )
    }

    /** Set cafeteria order */
    single {
        SetCafeteriaOrder(
            cafeteriaRepo = get()
        )
    }

    /** Reset cafeteria order */
    single {
        ResetCafeteriaOrder(
            cafeteriaRepo = get()
        )
    }

    /** Send app feedback */
    single {
        SendAppFeedback(
            context = get(),
            globalConfig = get()
        )
    }

    /** Login */
    single {
        Login(
            accountService = get()
        )
    }

    /** Remembered login */
    single {
        RememberedLogin(
            accountService = get()
        )
    }

    /** Get saved account */
    single {
        GetSavedAccount(
            accountService = get()
        )
    }

    /** Check new notice */
    single {
        GetNewNotice(
            noticeRepo = get(),
            globalConfig = get()
        )
    }

    /** Get all notices */
    single {
        GetAllNotices(
            noticeRepo = get(),
            globalConfig = get()
        )
    }

    /** Dismiss notice */
    single {
        DismissNotice(
            noticeRepo = get()
        )
    }

    /** Check for update */
    single {
        CheckForUpdate(
            versionRepo = get()
        )
    }

    /** Ask */
    single {
        Ask(
            interactionRepo = get()
        )
    }

    /** Get questions ans answers */
    single {
        GetQuestionsAndAnswers(
            interactionRepo = get()
        )
    }

    /** Mark answer read */
    single {
        MarkAnswerRead(
            interactionRepo = get()
        )
    }

    /** Mark answer read */
    single {
        FetchNotifications(
            interactionRepo = get()
        )
    }

    /** Get FCM token */
    single {
        GetFirebaseToken()
    }
}