/**
 * Copyright (C) 2018-2019 INU Appcenter. All rights reserved.
 *
 * This file is part of INU Cafeteria.
 *
 * This work is licensed under the terms of the MIT license.
 * For a copy, see <https://opensource.org/licenses/MIT>.
 */

package com.inu.cafeteria.service

import com.google.gson.JsonElement
import com.inu.cafeteria.model.scheme.*
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

/**
 * App-wide network service.
 */
interface CafeteriaNetworkService {
    @GET("/version.json")
    fun getVersionResult(): Call<Version>

    @POST("/login")
    fun getLoginResult(@Body data: LoginParams): Call<LoginResult>

    @POST("/logout")
    fun getLogoutResult(@Body data: LogoutParams): Call<LogoutResult>

    @POST("/activeBarcode")
    fun getActivateBarcodeResult(@Body data: ActivateBarcodeParams): Call<ActivateBarcodeResult>

    @GET("/notice.json")
    fun getNotice(): Call<Notice>

    @GET("/food/{date}")
    fun getFoods(@Path("date") date: String): Call<JsonElement>

    @GET("/cafecode.json")
    fun getCafeteria(): Call<JsonElement>
}