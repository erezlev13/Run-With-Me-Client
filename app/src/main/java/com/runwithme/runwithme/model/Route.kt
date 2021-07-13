package com.runwithme.runwithme.model

import com.google.gson.annotations.SerializedName

data class Route(
    @SerializedName("coordinates")
    val coordinates: ArrayList<DoubleArray>
)
