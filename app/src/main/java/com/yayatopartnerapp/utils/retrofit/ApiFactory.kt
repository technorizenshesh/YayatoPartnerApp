package com.yayatotaxi.utils.retrofit

import android.content.Context
import android.content.SharedPreferences
import com.androidnetworking.interceptors.HttpLoggingInterceptor
import com.yayatopartnerapp.utils.AppConstant
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ApiFactory {

    companion object {
        var BASE_URL: String = AppConstant.BASE_URL
        private var retrofit: Retrofit? = null
        private val httpClient = OkHttpClient.Builder()

        fun getClientWithoutHeader(context: Context): Retrofit? {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            val client = httpClient.addInterceptor(interceptor)
                .connectTimeout(5, TimeUnit.MINUTES)
                .readTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .build()
            if (retrofit == null) {
                retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }
            return retrofit
        }
    }

}