package com.runwithme.runwithme.model

import com.google.gson.annotations.SerializedName
import java.time.LocalDate

data class User(
    @SerializedName("_id")
    val _id: String,
    @SerializedName("firstName")
    val firstName: String,
    @SerializedName("lastName")
    val lastName: String,
    @SerializedName("email")
    val email: String,
)
