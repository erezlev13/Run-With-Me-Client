package com.runwithme.runwithme.model.network

import com.google.gson.annotations.SerializedName
import com.runwithme.runwithme.model.GroupRun

data class ScheduleRunResponse(
    @SerializedName("groupRun")
    val groupRun: GroupRun
)
