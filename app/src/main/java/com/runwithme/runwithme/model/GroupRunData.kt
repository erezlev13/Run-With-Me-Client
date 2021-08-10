package com.runwithme.runwithme.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class GroupRunData(
    @SerializedName("_id")
    var _id: String,
    @SerializedName("membersRuns")
    var membersRuns: ArrayList<Run>,
): Serializable
