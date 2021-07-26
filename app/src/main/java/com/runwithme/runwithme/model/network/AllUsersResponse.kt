package com.runwithme.runwithme.model.network

import com.google.gson.annotations.SerializedName
import com.runwithme.runwithme.model.User

data class AllUsersResponse(
    @SerializedName("users")
    var users: List<User>
)
