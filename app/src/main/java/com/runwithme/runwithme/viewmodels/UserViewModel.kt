package com.runwithme.runwithme.viewmodels

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.runwithme.runwithme.data.database.UserEntity
import com.runwithme.runwithme.model.User
import com.runwithme.runwithme.network.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class UserViewModel @Inject constructor(
    private val repository: Repository,
    application: Application
) : AndroidViewModel(application) {


    val readUser : LiveData<List<UserEntity>> = repository.local.readUser().asLiveData()

    fun updateUser(userEntity: UserEntity){
        viewModelScope.launch(Dispatchers.IO){
            repository.local.updateUser(userEntity)
            val userToUpdate = User(userEntity.user._id,userEntity.user.firstName
                ,userEntity.user.lastName,userEntity.user.email,userEntity.user.photoUri
            ,userEntity.user.runs)
            repository.remote.updateMe(userToUpdate)
        }
    }

    fun deleteUser(userEntity: UserEntity){
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.deleteUser(userEntity)
        }
    }

}