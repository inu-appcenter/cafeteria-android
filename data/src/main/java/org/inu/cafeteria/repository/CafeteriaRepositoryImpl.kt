package org.inu.cafeteria.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.JsonElement
import org.inu.cafeteria.exception.ResponseFailException
import org.inu.cafeteria.model.json.Cafeteria
import org.inu.cafeteria.service.CafeteriaNetworkService
import retrofit2.Call


class CafeteriaRepositoryImpl(
    private val networkService: CafeteriaNetworkService
) : CafeteriaRepository() {

    override fun getAllCafeteria(): Call<JsonElement> {
        return networkService.getCafeteria()
    }
}