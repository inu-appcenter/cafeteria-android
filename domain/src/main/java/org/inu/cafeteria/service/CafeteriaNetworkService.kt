package org.inu.cafeteria.service

import org.inu.cafeteria.model.scheme.*
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

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
}