package com.runwithme.runwithme.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Route(
    @SerializedName("_id")
    var _id: String,
    @SerializedName("coordinates")
    val coordinates: ArrayList<DoubleArray>
) : Serializable
