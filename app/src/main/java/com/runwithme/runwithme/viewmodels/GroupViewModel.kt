package com.runwithme.runwithme.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.runwithme.runwithme.model.Group
import com.runwithme.runwithme.model.Run
import com.runwithme.runwithme.model.network.*
import com.runwithme.runwithme.network.Repository
import com.runwithme.runwithme.utils.Constants
import com.runwithme.runwithme.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class GroupViewModel@Inject constructor(
    private val repository: Repository,
    application: Application
) : AndroidViewModel(application) {

    val groupData: MutableLiveData<NetworkResult<Group>> = MutableLiveData()
    val myGroupsResponse : MutableLiveData<NetworkResult<MyGroupsResponse>> = MutableLiveData()


    fun saveGroupData(groupDataRequest: GroupDataRequest) {
        viewModelScope.launch {
            try {
                val response = repository.remote.saveGroupData(groupDataRequest)
                groupData.value = handleGroupDataResponse(response)
            } catch (e: Exception) {
                groupData.value = NetworkResult.Error(Constants.NO_CONNECTION)
            }
        }
    }

    private fun handleGroupDataResponse(response: Response<Group>): NetworkResult<Group>? {
        when {
            response.isSuccessful -> {
                val groupDataBody = response.body()
                return NetworkResult.Success(groupDataBody!!)
            }
            else ->{
                val jsonObj = JSONObject(response.errorBody()!!.charStream().readText())
                return NetworkResult.Error(jsonObj.getString("message"))
            }
        }
    }

    fun getMyGroups() = viewModelScope.launch {
        try {
            val response = repository.remote.getMyGroups()
            myGroupsResponse.value = handleMyGroupsResponse(response)
        } catch (e: Exception) {
            myGroupsResponse.value = NetworkResult.Error("No Connection")
        }
    }

    private fun handleMyGroupsResponse(response: Response<MyGroupsResponse>): NetworkResult<MyGroupsResponse>? {
        when {
            response.isSuccessful -> {
                val groupsResponse = response.body()
                return NetworkResult.Success(groupsResponse!!)
            }
            else ->{
                val jsonObj = JSONObject(response.errorBody()!!.charStream().readText())
                return NetworkResult.Error(jsonObj.getString("message"))
            }
        }
    }
}