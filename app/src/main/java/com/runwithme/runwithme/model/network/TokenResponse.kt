package com.runwithme.runwithme.model.network

import com.google.gson.annotations.SerializedName

data class TokenResponse(
    @SerializedName("isValidToken")
    var isValidToken: Boolean
)
