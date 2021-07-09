package com.runwithme.runwithme.model

import com.google.gson.annotations.SerializedName

data class LoginResponse (

    @SerializedName("token")
    var token: String,

    @SerializedName("user")
    var user: User
)
