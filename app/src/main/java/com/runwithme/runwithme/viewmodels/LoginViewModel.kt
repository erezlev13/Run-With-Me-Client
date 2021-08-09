package com.runwithme.runwithme.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.runwithme.runwithme.data.database.UserEntity
import com.runwithme.runwithme.model.network.LoginRequest
import com.runwithme.runwithme.model.network.LoginResponse
import com.runwithme.runwithme.model.network.SignupRequest
import com.runwithme.runwithme.model.network.TokenResponse
import com.runwithme.runwithme.network.Repository
import com.runwithme.runwithme.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: Repository,
    application: Application
) : AndroidViewModel(application){

    //Room Database
    val readUser : LiveData<List<UserEntity>> = repository.local.readUserForCoroutine().asLiveData()

    private fun insertUser(userEntity: UserEntity) =
        viewModelScope.launch(Dispatchers.IO){
            repository.local.insertUser(userEntity)
        }

    //Retrofit
    var loginResponse: MutableLiveData<NetworkResult<LoginResponse>> = MutableLiveData()
    val tokenResponse : MutableLiveData<NetworkResult<TokenResponse>> = MutableLiveData()


    fun login(loginRequest : LoginRequest) = viewModelScope.launch{
        performLogin(loginRequest)
    }

    private suspend fun performLogin(loginRequest : LoginRequest){
        try {
            val response = repository.remote.login(loginRequest)
            loginResponse.value = handleLoginResponse(response)
        } catch (e: Exception) {
            loginResponse.value = NetworkResult.Error("Wrong Username or Password Please try again!")
        }
    }

    fun signup(signupRequest: SignupRequest) = viewModelScope.launch{
        performSignup(signupRequest)
    }

    private suspend fun performSignup(signupRequest: SignupRequest){
        try {
            val response = repository.remote.signup(signupRequest)
            loginResponse.value = handleLoginResponse(response)
        } catch (e: Exception) {
            loginResponse.value = NetworkResult.Error("No Connection")
        }
    }

    private fun handleLoginResponse(response: Response<LoginResponse>): NetworkResult<LoginResponse>? {
        when {
            response.isSuccessful -> {
                val loginResponse = response.body()
                if(loginResponse != null){
                    offlineCacheUser(loginResponse!!)
                }
                return NetworkResult.Success(loginResponse!!)
            }
            else ->{
                val jsonObj = JSONObject(response.errorBody()!!.charStream().readText())
                return NetworkResult.Error(jsonObj.getString("message"))
            }
        }
    }
    private fun offlineCacheUser(loginResponse: LoginResponse) {
        val userEntity = UserEntity(loginResponse.token, loginResponse.user)
        insertUser(userEntity)
    }

    fun isValidToken(token : String) =viewModelScope.launch {
        checkToken(token)
    }

    private suspend fun checkToken(token :String){
        try {
            val response = repository.remote.isValidToken(token)
            tokenResponse.value = handleTokenResponse(response)
        } catch (e: Exception) {
            tokenResponse.value = NetworkResult.Error("No Connection")
        }
    }

    private fun handleTokenResponse(response: Response<TokenResponse>): NetworkResult<TokenResponse>? {
        when {
            response.isSuccessful -> {
                val tokenResponse = response.body()
                return NetworkResult.Success(tokenResponse!!)
            }
            else ->{
                val jsonObj = JSONObject(response.errorBody()!!.charStream().readText())
                return NetworkResult.Error(jsonObj.getString("message"))
            }
        }
    }

}