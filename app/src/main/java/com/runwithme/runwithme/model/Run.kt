package com.runwithme.runwithme.model

import com.google.gson.annotations.SerializedName
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime


data class Run(
    @SerializedName("date")
    val date: LocalDate,
    @SerializedName("startTime")
    val startTime: LocalTime,
    @SerializedName("endTime")
    val endTime: LocalTime,
    @SerializedName("runType")
    val runType: RunType,
    @SerializedName("runData")
    val runData: RunData,
    )
