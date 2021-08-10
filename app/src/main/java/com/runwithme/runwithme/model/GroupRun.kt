package com.runwithme.runwithme.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class GroupRun(
    @SerializedName("_id")
    var _id: String,
    @SerializedName("runners")
    var runners: ArrayList<User>,
    @SerializedName("location")
    var location: String,
    @SerializedName("date")
    var date: String,
    @SerializedName("groupRunData")
    var groupRunData: GroupRunData,
) : Serializable
