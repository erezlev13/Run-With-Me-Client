package com.runwithme.runwithme.network

import com.runwithme.runwithme.model.network.LoginRequest
import com.runwithme.runwithme.model.network.LoginResponse
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val runWithMeApi: RunWithMeService
) {
    suspend fun login(loginRequest: LoginRequest): Response<LoginResponse> {
        return runWithMeApi.login(loginRequest)
    }

}