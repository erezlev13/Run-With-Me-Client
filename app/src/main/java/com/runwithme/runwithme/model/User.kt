package com.runwithme.runwithme.model

import android.graphics.Bitmap
import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.time.LocalDate

data class User(
    @SerializedName("_id")
    var _id: String,
    @SerializedName("firstName")
    var firstName: String,
    @SerializedName("lastName")
    var lastName: String,
    @SerializedName("email")
    var email: String,
    @SerializedName("photoUri")
    var photoUri : String,
    @SerializedName("friends")
    var friends : ArrayList<String>,
    @SerializedName("runs")
    var runs : ArrayList<Run>,
    @SerializedName("groups")
    var groups : ArrayList<String>,
) : Serializable
