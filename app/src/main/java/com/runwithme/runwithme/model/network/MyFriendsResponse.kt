package com.runwithme.runwithme.model.network

import com.google.gson.annotations.SerializedName
import com.runwithme.runwithme.model.User

data class MyFriendsResponse(
    @SerializedName("myFriends")
    var friends: ArrayList<User>
)
