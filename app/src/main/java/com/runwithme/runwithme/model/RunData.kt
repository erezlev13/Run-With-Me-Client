package com.runwithme.runwithme.model

import com.google.gson.annotations.SerializedName

data class RunData(
    @SerializedName("distance")
    val distance: Int,
    @SerializedName("steps")
    val steps: Int,
    @SerializedName("averageSpeed")
    val averageSpeed: Float,
    @SerializedName("route")
    val route: Route
)
