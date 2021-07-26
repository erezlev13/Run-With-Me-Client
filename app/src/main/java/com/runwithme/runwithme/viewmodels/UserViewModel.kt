package com.runwithme.runwithme.viewmodels

import android.app.Application
import android.net.Uri
import androidx.lifecycle.*
import com.runwithme.runwithme.data.database.UserEntity
import com.runwithme.runwithme.model.User
import com.runwithme.runwithme.model.network.AllUsersResponse
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
class UserViewModel @Inject constructor(
    private val repository: Repository,
    application: Application
) : AndroidViewModel(application) {


    val readUser : LiveData<List<UserEntity>> = repository.local.readUser().asLiveData()
    val allUsersResponse : MutableLiveData<NetworkResult<AllUsersResponse>> = MutableLiveData()

    fun updateUser(userEntity: UserEntity){
        viewModelScope.launch(Dispatchers.IO){
            repository.local.updateUser(userEntity)
            val userToUpdate = User(userEntity.user._id,userEntity.user.firstName
                ,userEntity.user.lastName,userEntity.user.email,userEntity.user.photoUri
            ,userEntity.user.friends,userEntity.user.runs)
            repository.remote.updateMe(userToUpdate)
        }
    }

    fun deleteUser(userEntity: UserEntity){
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.deleteUser(userEntity)
        }
    }
    fun getAllUsers() =viewModelScope.launch {
        try {
            val response = repository.remote.getAllUsers()
            allUsersResponse.value = handleAllUsersResponse(response)
        } catch (e: Exception) {
            allUsersResponse.value = NetworkResult.Error("No Connection")
        }
    }



    private fun handleAllUsersResponse(response: Response<AllUsersResponse>): NetworkResult<AllUsersResponse>? {
        when {
            response.isSuccessful -> {
                val allUsersResponse = response.body()
                return NetworkResult.Success(allUsersResponse!!)
            }
            else ->{
                val jsonObj = JSONObject(response.errorBody()!!.charStream().readText())
                return NetworkResult.Error(jsonObj.getString("message"))
            }
        }
    }

}