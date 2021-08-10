package com.runwithme.runwithme.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime


data class Run(
    @SerializedName("_id")
    var _id: String,
    @SerializedName("date")
    val date: String,
    @SerializedName("startTime")
    val startTime: String,
    @SerializedName("endTime")
    val endTime: String,
    @SerializedName("runType")
    val runType: String,
    @SerializedName("runData")
    val runData: RunData,
    ) :Serializable
