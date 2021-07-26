package com.runwithme.runwithme.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Group(
    @SerializedName("_id")
    var _id: String,
    @SerializedName("name")
    var name: String,
    @SerializedName("groupCreator")
    var groupCreator: User,
    @SerializedName("groupRuns")
    var groupRuns: ArrayList<GroupRun>,
    @SerializedName("groupMembers")
    var groupMembers: ArrayList<User>,
) : Serializable
