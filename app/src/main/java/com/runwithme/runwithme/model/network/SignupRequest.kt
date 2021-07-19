package com.runwithme.runwithme.model.network

import com.google.gson.annotations.SerializedName

data class SignupRequest(
    @SerializedName("firstName")
    var firstName: String,
    @SerializedName("lastName")
    var lastName: String,
    @SerializedName("email")
    var email: String,
    @SerializedName("password")
    var password: String

)
