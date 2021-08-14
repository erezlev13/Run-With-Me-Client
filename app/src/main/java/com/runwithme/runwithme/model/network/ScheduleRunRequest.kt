package com.runwithme.runwithme.model.network

import com.google.gson.annotations.SerializedName

data class ScheduleRunRequest (
    @SerializedName("groupId")
    private var groupId: String,
    @SerializedName("location")
    private var location: String,
    @SerializedName("date")
    private var date: String,
    @SerializedName("time")
    private var time: String,
)