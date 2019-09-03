package org.inu.cafeteria.repository

import androidx.lifecycle.LiveData
import com.google.gson.JsonElement
import org.inu.cafeteria.model.json.Cafeteria
import retrofit2.Call

abstract class CafeteriaRepository : Repository() {

    abstract fun getAllCafeteria(): Call<JsonElement>
}