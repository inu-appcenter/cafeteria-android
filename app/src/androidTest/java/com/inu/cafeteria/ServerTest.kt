package com.inu.cafeteria

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.GrantPermissionRule
import com.inu.cafeteria.injection.myModules
import com.inu.cafeteria.usecase.GetVersion
import org.junit.After

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Before
import org.junit.Rule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest
import org.koin.test.inject
import timber.log.Timber

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ServerTest : KoinTest{
    @get:Rule
    var permissionRule = GrantPermissionRule.grant(android.Manifest.permission.INTERNET)

    fun getContext(): Context = ApplicationProvider.getApplicationContext()

    @Before
    fun before() {
        Timber.plant(Timber.DebugTree())

        stopKoin()

        startKoin {
            androidContext(getContext())
            modules(myModules)
        }
    }

    @After
    fun after() {
        Timber.uprootAll()

        stopKoin()
    }

    @Test
    fun versionTest() {
        Timber.i("HI!!")

        val getVersion: GetVersion by inject()

        getVersion(Unit) {
            it.onSuccess {
                Timber.i("Current version is ${it.currentVersion}, latest is ${it.latestVersion}, so update needed? ${it.needUpdate()}.")
            }.onError {
                Timber.e("No!!")
            }
        }

        Thread.sleep(5000)
    }

}
