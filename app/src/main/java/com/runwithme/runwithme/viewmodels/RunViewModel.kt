package com.runwithme.runwithme.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.runwithme.runwithme.model.Run
import com.runwithme.runwithme.model.network.RunDataRequest
import com.runwithme.runwithme.network.Repository
import com.runwithme.runwithme.utils.Constants.NO_CONNECTION
import com.runwithme.runwithme.utils.NetworkResult
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.lifecycle.HiltViewModel
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

    fun saveRunData(runDataRequest: RunDataRequest) {
        viewModelScope.launch {
            try {
                val response = repository.remote.saveRunData(runDataRequest)
                runData.value = handleRunDataResponse(response)
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
}