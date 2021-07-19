package com.runwithme.runwithme.data.database

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.runwithme.runwithme.model.User
import com.runwithme.runwithme.utils.Constants.USER_TABLE


@Entity(tableName = USER_TABLE)
class UserEntity(
    var token : String,
    var user : User,
) {
    @PrimaryKey(autoGenerate = false)
    var id : Int = 0
}