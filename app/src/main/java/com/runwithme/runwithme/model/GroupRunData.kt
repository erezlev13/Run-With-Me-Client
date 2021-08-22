package com.runwithme.runwithme.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class GroupRunData(
    @SerializedName("_id")
    var _id: String,
    @SerializedName("membersRuns")
    var membersRuns: ArrayList<Run>,
    @SerializedName("averageDistance")
    var averageDistance: Float,
    @SerializedName("averageSteps")
    var averageSteps: Int,
    @SerializedName("averageOfAveragePace")
    var averageOfAveragePace: String,
): Serializable
