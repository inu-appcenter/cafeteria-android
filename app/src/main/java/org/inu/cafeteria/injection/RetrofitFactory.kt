package org.inu.cafeteria.injection

import android.content.Context
import com.franmontiel.persistentcookiejar.PersistentCookieJar
import com.franmontiel.persistentcookiejar.cache.SetCookieCache
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor
import okhttp3.OkHttpClient
import org.inu.cafeteria.repository.PrivateRepositoryImpl
import org.inu.cafeteria.service.CafeteriaNetworkService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitFactory {
    companion object {
        fun createCafeteriaNetworkService(context: Context): CafeteriaNetworkService {
            val cookieJar = PersistentCookieJar(
                SetCookieCache(),
                SharedPrefsCookiePersistor(context)
            )
            val okHttpClient = OkHttpClient.Builder()
                .cookieJar(cookieJar)
                .build()

            val builder = Retrofit.Builder()

            // You should implement your own PrivateRepository.
            // @see [PrivateRepository]
            val retrofit = builder
                .baseUrl(PrivateRepositoryImpl().getServerBaseUrl())
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build()

            return retrofit.create(CafeteriaNetworkService::class.java)
        }
    }
}