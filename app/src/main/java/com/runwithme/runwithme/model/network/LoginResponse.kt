package com.runwithme.runwithme.model.network

import com.google.gson.annotations.SerializedName
import com.runwithme.runwithme.model.User

data class LoginResponse (
    @SerializedName("token")
    var token: String,
    @SerializedName("user")
    var user: User
)
