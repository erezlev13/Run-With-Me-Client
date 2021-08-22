package com.runwithme.runwithme.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.runwithme.runwithme.model.Group
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
    val myTodayGroupRunsResponse : MutableLiveData<NetworkResult<MyTodayGroupRunsResponse>> = MutableLiveData()
    val scheduleRun: MutableLiveData<NetworkResult<ScheduleRunResponse>> = MutableLiveData()
    val futureGroupRunResponse : MutableLiveData<NetworkResult<FutureGroupRunResponse>> = MutableLiveData()
    val pastGroupRunResponse : MutableLiveData<NetworkResult<PastGroupRunResponse>> = MutableLiveData()



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
            Log.d("myapp","${e.message}")
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


    fun getMyTodayGroupRuns() = viewModelScope.launch {
        try {
            val response = repository.remote.getMyTodayGroupRuns()
            myTodayGroupRunsResponse.value = handleMyTodayGroupRunsResponse(response)
        } catch (e: Exception) {
            myTodayGroupRunsResponse.value = NetworkResult.Error("No Connection")
        }
    }

    private fun handleMyTodayGroupRunsResponse(response: Response<MyTodayGroupRunsResponse>): NetworkResult<MyTodayGroupRunsResponse>? {
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

    fun saveScheduleRun(scheduleRunRequest: ScheduleRunRequest) {
        viewModelScope.launch {
            try {
                val response = repository.remote.saveScheduleRun(scheduleRunRequest)
                scheduleRun.value = handleScheduleRunResponse(response)
            } catch (e: Exception) {
                scheduleRun.value = NetworkResult.Error(Constants.NO_CONNECTION)
            }
        }
    }

    private fun handleScheduleRunResponse(response: Response<ScheduleRunResponse>): NetworkResult<ScheduleRunResponse> {
        when {
            response.isSuccessful -> {
                val groupRunDataBody = response.body()
                return NetworkResult.Success(groupRunDataBody!!)
            }
            else ->{
                val jsonObj = JSONObject(response.errorBody()!!.charStream().readText())
                return NetworkResult.Error(jsonObj.getString("message"))
            }
        }
    }

    fun getFutureGroupRuns(groupId:String) {
        viewModelScope.launch {
            try {
                val response = repository.remote.getFutureGroupRuns(groupId)
                futureGroupRunResponse.value = handleFutureGroupRunsResponse(response)
            } catch (e: Exception) {
                futureGroupRunResponse.value = NetworkResult.Error(Constants.NO_CONNECTION)
            }
        }
    }

    private fun handleFutureGroupRunsResponse(response: Response<FutureGroupRunResponse>): NetworkResult<FutureGroupRunResponse> {
        when {
            response.isSuccessful -> {
                val futureGroupRunsResponse = response.body()
                return NetworkResult.Success(futureGroupRunsResponse!!)
            }
            else ->{
                val jsonObj = JSONObject(response.errorBody()!!.charStream().readText())
                return NetworkResult.Error(jsonObj.getString("message"))
            }
        }
    }

    fun getPastGroupRuns(groupId:String) {
        viewModelScope.launch {
            try {
                val response = repository.remote.getPastGroupRuns(groupId)
                pastGroupRunResponse.value = handlePastGroupRunsResponse(response)
            } catch (e: Exception) {
                pastGroupRunResponse.value = NetworkResult.Error(Constants.NO_CONNECTION)
            }
        }
    }

    private fun handlePastGroupRunsResponse(response: Response<PastGroupRunResponse>): NetworkResult<PastGroupRunResponse> {
        when {
            response.isSuccessful -> {
                val pastGroupRunsResponse = response.body()
                return NetworkResult.Success(pastGroupRunsResponse!!)
            }
            else ->{
                val jsonObj = JSONObject(response.errorBody()!!.charStream().readText())
                return NetworkResult.Error(jsonObj.getString("message"))
            }
        }
    }
}