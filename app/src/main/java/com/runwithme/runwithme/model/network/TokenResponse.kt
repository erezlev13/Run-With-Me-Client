package com.runwithme.runwithme.model.network

import com.google.gson.annotations.SerializedName
import com.runwithme.runwithme.model.User

data class TokenResponse(
    @SerializedName("isValidToken")
    var isValidToken: Boolean,
    @SerializedName("token")
    var token: String,
    @SerializedName("user")
    var user: User
)
