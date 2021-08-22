package com.runwithme.runwithme.model.network

import com.google.gson.annotations.SerializedName
import com.runwithme.runwithme.model.GroupRun

data class PastGroupRunResponse(
    @SerializedName("groupRuns")
    var pastGroupRuns: ArrayList<GroupRun>
)
