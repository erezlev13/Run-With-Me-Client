package com.runwithme.runwithme.model.network

import com.google.gson.annotations.SerializedName
import com.runwithme.runwithme.model.Group
import com.runwithme.runwithme.model.GroupRun

data class FutureGroupRunResponse(
    @SerializedName("groupRuns")
    var groupRuns: ArrayList<GroupRun>
)
