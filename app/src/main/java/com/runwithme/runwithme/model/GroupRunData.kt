package com.runwithme.runwithme.model

import com.google.gson.annotations.SerializedName

data class GroupRunData(
    @SerializedName("_id")
    var _id: String,
    @SerializedName("numberOfRunners")
    var numberOfRunners: Int,
    @SerializedName("totalDistance")
    var totalDistance: Int,
    @SerializedName("groupRunData")
    var averageSpeed: Float,
    @SerializedName("membersRuns")
    var membersRuns: ArrayList<Run>,
)
