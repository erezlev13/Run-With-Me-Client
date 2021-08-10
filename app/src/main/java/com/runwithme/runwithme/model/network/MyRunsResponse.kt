package com.runwithme.runwithme.model.network

import com.google.gson.annotations.SerializedName
import com.runwithme.runwithme.model.Run

data class MyRunsResponse(
    @SerializedName("myRuns")
    var runs: ArrayList<Run>
)
