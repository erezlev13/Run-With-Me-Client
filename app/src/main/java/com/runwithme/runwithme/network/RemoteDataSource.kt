package com.runwithme.runwithme.network

import android.util.Log
import com.runwithme.runwithme.model.*
import com.runwithme.runwithme.model.network.*
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Query
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val runWithMeApi: RunWithMeService
) {
    suspend fun login(loginRequest: LoginRequest): Response<LoginResponse> {
        return runWithMeApi.login(loginRequest)
    }
    suspend fun signup(signupRequest: SignupRequest): Response<LoginResponse> {
        return runWithMeApi.signup(signupRequest)
    }
    suspend fun isValidToken(@Query("token") token:String) : Response<TokenResponse>{
        return runWithMeApi.isValidToken(token)
    }
    suspend fun updateMe(user:User) : Response<User> {
        return runWithMeApi.updateMe(user)
    }
    suspend fun getAllUsers() : Response<AllUsersResponse>{
        return runWithMeApi.getAllUsers()
    }
    suspend fun addFriend(friendID : String) : Response<User>{
        return runWithMeApi.addFriend(friendID)
    }

}