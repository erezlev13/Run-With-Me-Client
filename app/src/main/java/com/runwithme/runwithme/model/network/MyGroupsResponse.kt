package com.runwithme.runwithme.model.network

import com.google.gson.annotations.SerializedName
import com.runwithme.runwithme.model.Group

data class MyGroupsResponse(
    @SerializedName("myGroups")
    var groups: ArrayList<Group>
)
