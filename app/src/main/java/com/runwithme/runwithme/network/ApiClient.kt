package com.runwithme.runwithme.network

import android.content.Context
import android.util.Log
import com.runwithme.runwithme.utils.Constants.BASE_URL
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiClient {
    private lateinit var apiService: RunWithMeService

    fun getApiService(context: Context): RunWithMeService {

        // Initialize ApiService if not initialized yet
        if (!::apiService.isInitialized) {
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okhttpClient(context)) // Add our Okhttp client
                .build()
            apiService = retrofit.create(RunWithMeService::class.java)
        }

        return apiService
    }

    /**
     * Initialize OkhttpClient with our interceptor
     */
    private fun okhttpClient(context: Context): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(context))
            .build()
    }

}