package com.runwithme.runwithme.model

import com.google.gson.annotations.SerializedName

data class GroupRun(
    @SerializedName("_id")
    var _id: String,
    @SerializedName("runners")
    var runners: ArrayList<User>,
    @SerializedName("groupRunData")
    var groupRunData: GroupRunData,
)
