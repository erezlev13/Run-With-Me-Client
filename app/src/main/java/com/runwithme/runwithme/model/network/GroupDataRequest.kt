package com.runwithme.runwithme.model.network

import com.google.gson.annotations.SerializedName

data class GroupDataRequest(
    @SerializedName("name")
    var name: String,
    @SerializedName("description")
    var description : String,
    @SerializedName("photoUri")
    var photoUri: String,
    @SerializedName("groupMembers")
    var groupMembers: ArrayList<String>,
)
