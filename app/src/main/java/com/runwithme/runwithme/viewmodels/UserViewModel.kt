package com.runwithme.runwithme.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.runwithme.runwithme.data.database.UserEntity
import com.runwithme.runwithme.model.User
import com.runwithme.runwithme.model.network.AllUsersResponse
import com.runwithme.runwithme.model.network.MyFriendsResponse
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


    val readUser : LiveData<List<UserEntity>> = repository.local.readUserForCoroutine().asLiveData()
    val allUsersResponse : MutableLiveData<NetworkResult<AllUsersResponse>> = MutableLiveData()
    val myFriendsResponse :  MutableLiveData<NetworkResult<MyFriendsResponse>> = MutableLiveData()

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

    fun addFriend(userToUpdate: UserEntity,friendID : String){
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.updateUser(userToUpdate)
            repository.remote.addFriend(friendID)
        }
    }

    fun deleteFriend(userToUpdate: UserEntity,friendID : String){
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.updateUser(userToUpdate)
            repository.remote.deleteFriend(friendID)
        }
    }

    fun getAllFriends() = viewModelScope.launch {
        try {
            val response = repository.remote.getAllFriends()
            myFriendsResponse.value = handleFriendsResponse(response)
        } catch (e: Exception) {
            myFriendsResponse.value = NetworkResult.Error("No Connection")
        }
    }

    private fun handleFriendsResponse(response: Response<MyFriendsResponse>): NetworkResult<MyFriendsResponse>? {
        when {
            response.isSuccessful -> {
                val friendsResponse = response.body()
                return NetworkResult.Success(friendsResponse!!)
            }
            else ->{
                val jsonObj = JSONObject(response.errorBody()!!.charStream().readText())
                return NetworkResult.Error(jsonObj.getString("message"))
            }
        }
    }

}