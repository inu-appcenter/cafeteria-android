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
import android.content.pm.PackageManager
import android.os.Build
import com.franmontiel.persistentcookiejar.PersistentCookieJar
import com.franmontiel.persistentcookiejar.cache.SetCookieCache
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor
import com.inu.cafeteria.config.Config
import com.inu.cafeteria.retrofit.CafeteriaNetworkService
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitFactory {

    // Original at https://jitpack.io/#danielceinos/Cooper
    class CooperInterceptor(private val context: Context) : Interceptor {

        private val userAgent: String by lazy {
            buildUserAgent(context)
        }

        override fun intercept(chain: Interceptor.Chain): Response {
            val builder = chain.request().newBuilder()
            builder.header("User-Agent", userAgent)
            return chain.proceed(builder.build())
        }

        private fun buildUserAgent(context: Context): String {
            with(context.packageManager) {
                val versionName = try {
                    getPackageInfo(context.packageName, 0).versionName
                } catch (e: PackageManager.NameNotFoundException) {
                    "nameNotFound"
                }
                val versionCode = try {
                    getPackageInfo(context.packageName, 0).versionCode.toString()
                } catch (e: PackageManager.NameNotFoundException) {
                    "versionCodeNotFound"
                }

                val appName = context.packageName
                val manufacturer = Build.MANUFACTURER
                val model = Build.MODEL
                val version = Build.VERSION.SDK_INT
                val versionRelease = Build.VERSION.RELEASE

                val installerName = getInstallerPackageName(context.packageName) ?: "StandAloneInstall"

                return "$appName / $versionName($versionCode); $installerName; ($manufacturer; $model; SDK $version; Android $versionRelease)"
            }
        }
    }

    companion object {

        fun createCafeteriaNetworkService(context: Context): CafeteriaNetworkService {
            val cookieJar = PersistentCookieJar(
                SetCookieCache(),
                SharedPrefsCookiePersistor(context)
            )
            val okHttpClient = OkHttpClient.Builder()
                .cookieJar(cookieJar)
                .addInterceptor(CooperInterceptor(context)) // Set user agent
                .build()

            val builder = Retrofit.Builder()

            val retrofit = builder
                .baseUrl(Config.baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build()

            return retrofit.create(CafeteriaNetworkService::class.java)
        }
    }
}