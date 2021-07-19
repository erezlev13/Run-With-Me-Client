package com.runwithme.runwithme.network

import com.runwithme.runwithme.model.network.LoginRequest
import com.runwithme.runwithme.model.network.LoginResponse
import com.runwithme.runwithme.model.Run
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface RunWithMeService {

    @GET("my-runs")
    fun getMyRuns(): Call<ArrayList<Run>>

    @POST("users/login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<LoginResponse>
}