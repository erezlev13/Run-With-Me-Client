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
    suspend fun isValidToken(token:String) : Response<TokenResponse>{
        return runWithMeApi.isValidToken(token)
    }
    suspend fun updateMe(user:User) : Response<User> {
        return runWithMeApi.updateMe(user)
    }
    suspend fun getAllUsers() : Response<AllUsersResponse>{
        return runWithMeApi.getAllUsers()
    }
    suspend fun getAllFriends() : Response<MyFriendsResponse>{
        return runWithMeApi.getAllFriends()
    }
    suspend fun getMyGroups() : Response<MyGroupsResponse>{
        return runWithMeApi.getMyGroups()
    }
    suspend fun getMyRuns() : Response<MyRunsResponse>{
        return runWithMeApi.getMyRuns()
    }
    suspend fun addFriend(friendID : String) : Response<User>{
        return runWithMeApi.addFriend(friendID)
    }
    suspend fun deleteFriend(friendID : String) : Response<User>{
        return runWithMeApi.deleteFriend(friendID)
    }
    suspend fun saveRunData(runDataRequest: RunDataRequest): Response<Run> {
        return runWithMeApi.saveRunData(runDataRequest)
    }
    suspend fun saveGroupData(groupDataRequest: GroupDataRequest): Response<Group> {
        return runWithMeApi.saveGroupData(groupDataRequest)
    }

}