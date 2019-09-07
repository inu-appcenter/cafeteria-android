package com.inu.cafeteria.common

import android.app.Application
import com.inu.cafeteria.injection.myModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

class ThisApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree())

        startKoin {
            androidContext(this@ThisApplication)
            modules(myModules)
        }
    }
}