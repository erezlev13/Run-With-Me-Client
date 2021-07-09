package com.runwithme.runwithme.network

import com.runwithme.runwithme.model.LoginRequest
import com.runwithme.runwithme.model.LoginResponse
import com.runwithme.runwithme.model.Run
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface RunWithMeService {

    @GET("my-runs")
    fun getMyRuns(): Call<ArrayList<Run>>

    @POST("users/login")
    fun login(@Body request: LoginRequest): Call<LoginResponse>
}