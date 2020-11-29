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

package com.inu.cafeteria.retrofit

import com.inu.cafeteria.retrofit.scheme.*
import retrofit2.Call
import retrofit2.http.*

/**
 * App-wide network service.
 */

interface CafeteriaNetworkService {

    /**
     * Notices
     */

    @GET("/notices")
    fun getAllNotices(): Call<List<NoticeResult>>

    @GET("/notices/latest")
    fun getLatestNotice(): Call<NoticeResult>


    /**
     * Versioning
     */

    @GET("/shouldIUpdate")
    fun shouldIUpdate(@Query("os") os: String, @Query("version") version: String): Call<Boolean>


    /**
     * Interactions
     */
    @POST("/ask")
    fun ask(@Body data: AskParams): Call<Unit>

    @GET("/questions")
    fun getAllQuestions(): Call<List<QuestionResult>>

    @GET("/answers")
    fun getAllAnswers(@Query("unreadOnly") unreadOnly: Boolean = false): Call<List<AnswerResult>>

    @POST("/markAnswerRead/{answerId}")
    fun markAnswerRead(@Path("answerId") answerId: Int): Call<Unit>


    /**
     * Cafeteria
     */

    @GET("/cafeteria")
    fun getCafeteria(): Call<List<CafeteriaResult>>

    @GET("/corners")
    fun getCorners(): Call<List<CornerResult>>

    @GET("/menus")
    fun getMenus(@Query("date") date: String? = null): Call<List<MenuResult>>


    /**
     * Membership
     */

    @POST("/login")
    fun getLoginResult(@Body data: LoginParams): Call<LoginResult>

    @PUT("/activateBarcode")
    fun getActivateBarcodeResult(): Call<Unit>
}