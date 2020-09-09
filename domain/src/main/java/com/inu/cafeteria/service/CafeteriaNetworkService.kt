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