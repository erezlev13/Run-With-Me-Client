package com.runwithme.runwithme.model.network

import com.google.gson.annotations.SerializedName
import com.runwithme.runwithme.model.Run
import com.runwithme.runwithme.model.User

data class SaveRunResponse(
    @SerializedName("newRun")
    var newRun: Run,

)
