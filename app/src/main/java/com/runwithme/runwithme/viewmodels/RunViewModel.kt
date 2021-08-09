package com.runwithme.runwithme.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.runwithme.runwithme.data.database.UserEntity
import com.runwithme.runwithme.model.Run
import com.runwithme.runwithme.model.User
import com.runwithme.runwithme.model.network.MyGroupsResponse
import com.runwithme.runwithme.model.network.MyRunsResponse
import com.runwithme.runwithme.model.network.RunDataRequest
import com.runwithme.runwithme.network.Repository
import com.runwithme.runwithme.utils.Constants.NO_CONNECTION
import com.runwithme.runwithme.utils.ExtensionFunctions.observeOnce
import com.runwithme.runwithme.utils.NetworkResult
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class RunViewModel @Inject constructor(
    private val repository: Repository,
    application: Application
) : AndroidViewModel(application) {

    val runData: MutableLiveData<NetworkResult<Run>> = MutableLiveData()
    val myRunsResponse : MutableLiveData<NetworkResult<MyRunsResponse>> = MutableLiveData()

    fun saveRunData(runDataRequest: RunDataRequest) {
        viewModelScope.launch {
            try {
                val response = repository.remote.saveRunData(runDataRequest)
                runData.value = handleRunDataResponse(response)
                addRunToLocalDB(response.body()!!)
            } catch (e: Exception) {
                runData.value = NetworkResult.Error(NO_CONNECTION)
            }
        }
    }

    private fun handleRunDataResponse(response: Response<Run>): NetworkResult<Run>? {
        when {
            response.isSuccessful -> {
                val runDataBody = response.body()
                return NetworkResult.Success(runDataBody!!)
            }
            else ->{
                val jsonObj = JSONObject(response.errorBody()!!.charStream().readText())
                return NetworkResult.Error(jsonObj.getString("message"))
            }
        }
    }

    private suspend fun addRunToLocalDB(run : Run){
        val readUser : UserEntity = repository.local.readUser().toList()[0]
        if(readUser != null){
            val user = readUser.user
            user.runs.add(run)
            val updatedUserEntity = UserEntity(readUser.token,user)
            repository.local.updateUser(updatedUserEntity)
        }

    }

    fun getMyRuns() = viewModelScope.launch {
        try {
            val response = repository.remote.getMyRuns()
            myRunsResponse.value = handleMyRunsResponse(response)
        } catch (e: Exception) {
            myRunsResponse.value = NetworkResult.Error("No Connection")
        }
    }

    private fun handleMyRunsResponse(response: Response<MyRunsResponse>): NetworkResult<MyRunsResponse>? {
        when {
            response.isSuccessful -> {
                val runsResponse = response.body()
                return NetworkResult.Success(runsResponse!!)
            }
            else ->{
                val jsonObj = JSONObject(response.errorBody()!!.charStream().readText())
                return NetworkResult.Error(jsonObj.getString("message"))
            }
        }
    }
}