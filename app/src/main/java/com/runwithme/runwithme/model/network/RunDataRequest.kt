package com.runwithme.runwithme.model.network

import android.location.LocationListener
import com.google.gson.annotations.SerializedName
import com.runwithme.runwithme.model.RunType
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.LocalTime

data class RunDataRequest(
    @SerializedName("start-time")
    var startTime: LocalTime,
    @SerializedName("end-time")
    var endTime: LocalTime,
    @SerializedName("run-date")
    var date: LocalDateTime,
    @SerializedName("average-pace")
    var avgPace: SimpleDateFormat,
    @SerializedName("distance")
    var distance: Float,
    @SerializedName("steps")
    var steps: Int,
    @SerializedName("locations")
    var locations: ArrayList<Pair<Double, Double>>,
    @SerializedName("run-type")
    var runType: RunType = RunType.PERSONAL
)