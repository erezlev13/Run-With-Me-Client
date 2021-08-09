package com.runwithme.runwithme.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class RunData(
    @SerializedName("_id")
    var _id: String,
    @SerializedName("distance")
    val distance: Float,
    @SerializedName("steps")
    val steps: Int,
    @SerializedName("averagePace")
    val averagePace: String,
    @SerializedName("route")
    val route: Route
) : Serializable
