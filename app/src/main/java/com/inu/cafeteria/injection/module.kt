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
import com.inu.cafeteria.BuildConfig
import com.inu.cafeteria.common.EventHub
import com.inu.cafeteria.common.navigation.Navigator
import com.inu.cafeteria.config.BuildConfigHolder
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

    /** Config */
    single {
        BuildConfigHolder(
            serverFlavor = BuildConfig.FLAVOR_server,
            versionName = BuildConfig.VERSION_NAME,
            applicationId = BuildConfig.APPLICATION_ID
        )
    }

    /** Navigator */
    single {
        Navigator(
            context = get()
        )
    }

    /** Network Service */
    single {
        RetrofitFactory.createCafeteriaNetworkService(
            context = get()
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

    /** Account service */
    single {
        AccountService(
            accountRepo = get()
        )
    }

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
            networkService = get()
        ) as InteractionRepository
    }

    /** Waiting order repository */
    single {
        WaitingOrderRepositoryImpl(
            networkService = get()
        ) as WaitingOrderRepository
    }

    /** External credentials repository */
    single {
        ExternalCredentialsRepositoryImpl(
            db = get()
        ) as ExternalCredentialsRepository
    }

    /** Onboarding hint repository */
    single {
        OnboardingHintRepositoryImpl(
            db = get()
        ) as OnboardingHintRepository
    }

    /** App usage repository */
    single {
        AppUsageRepositoryImpl(
            context = get(),
            db = get()
        ) as AppUsageRepository
    }

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

    /** Get menu supporting cafeteria */
    single {
        GetMenuSupportingCafeteria(
            cafeteriaRepo = get()
        )
    }

    /** Get menu supporting cafeteria without menus */
    single {
        GetMenuSupportingCafeteriaWithoutMenus(
            cafeteriaRepo = get()
        )
    }

    /** Get discount supporting cafeteria (without menus) */
    single {
        GetDiscountSupportingCafeteria(
            cafeteriaRepo = get()
        )
    }

    /** Get notification supporting cafeteria (without menus) */
    single {
        GetNotificationSupportingCafeteria(
            cafeteriaRepo = get()
        )
    }

    /** Get cafeteria order */
    single {
        GetSortingOrders(
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
            context = get()
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
            noticeRepo = get()
        )
    }

    /** Get all notices */
    single {
        GetAllNotices(
            noticeRepo = get()
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
        FetchUnreadAnswers(
            interactionRepo = get()
        )
    }

    /** Get FCM token */
    single {
        GetFirebaseToken()
    }

    /** Get waiting orders */
    single {
        GetWaitingOrders(
            waitingOrderRepo = get(),
            externalCredentialsRepo = get()
        )
    }

    /** Add waiting order */
    single {
        AddWaitingOrder(
            waitingOrderRepo = get(),
            externalCredentialsRepo = get()
        )
    }

    /** Delete waiting orders */
    single {
        DeleteWaitingOrder(
            waitingOrderRepo = get(),
            externalCredentialsRepo = get()
        )
    }

    single {
        GetCafeteriaComment(
            cafeteriaRepo = get()
        )
    }
}