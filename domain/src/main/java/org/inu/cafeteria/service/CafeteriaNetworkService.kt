package org.inu.cafeteria.service

import com.google.gson.JsonElement
import org.inu.cafeteria.model.json.Cafeteria
import org.inu.cafeteria.model.scheme.*
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