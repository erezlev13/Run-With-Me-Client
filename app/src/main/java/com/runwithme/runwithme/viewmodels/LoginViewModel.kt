package com.runwithme.runwithme.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.runwithme.runwithme.model.LoginRequest
import com.runwithme.runwithme.model.LoginResponse
import com.runwithme.runwithme.network.Repository
import com.runwithme.runwithme.network.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: Repository,
    application: Application
) : AndroidViewModel(application){


    var loginResponse: MutableLiveData<NetworkResult<LoginResponse>> = MutableLiveData()

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

    private fun handleLoginResponse(response: Response<LoginResponse>): NetworkResult<LoginResponse>? {
        when {
            response.isSuccessful -> {
                val loginResponse = response.body()
                return NetworkResult.Success(loginResponse!!)
            }
            else ->{
                val jsonObj = JSONObject(response.errorBody()!!.charStream().readText())
                return NetworkResult.Error(jsonObj.getString("message"))
            }
        }
    }
}