package com.runwithme.runwithme.network

import com.runwithme.runwithme.model.*
import com.runwithme.runwithme.model.network.LoginRequest
import com.runwithme.runwithme.model.network.LoginResponse
import com.runwithme.runwithme.model.network.SignupRequest
import com.runwithme.runwithme.model.network.TokenResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface RunWithMeService {

    @GET("my-runs")
    fun getMyRuns(): Call<ArrayList<Run>>

    @POST("users/login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<LoginResponse>

    @POST("users/signup")
    suspend fun signup(@Body signupRequest: SignupRequest): Response<LoginResponse>

    @GET("users/check-token")
    suspend fun isValidToken(@Query("token") token:String) : Response<TokenResponse>

    @PATCH("users/updateme")
    suspend fun updateMe(@Body user:User) : Response<User>
}