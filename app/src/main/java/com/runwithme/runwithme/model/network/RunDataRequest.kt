package com.runwithme.runwithme.model.network

import com.google.gson.annotations.SerializedName
import com.runwithme.runwithme.model.RunType

data class RunDataRequest(
    @SerializedName("startTime")
    var startTime: String,
    @SerializedName("endTime")
    var endTime: String,
    @SerializedName("runDate")
    var date: String,
    @SerializedName("averagePace")
    var avgPace: String,
    @SerializedName("distance")
    var distance: Float,
    @SerializedName("steps")
    var steps: Int,
    @SerializedName("locations")
    var locations: ArrayList<Pair<Double, Double>>,
    @SerializedName("runType")
    var runType: RunType,
    @SerializedName("groupRunId")
    var groupRunId: String?
)