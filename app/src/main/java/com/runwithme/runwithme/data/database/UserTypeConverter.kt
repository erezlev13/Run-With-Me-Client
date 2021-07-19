package com.runwithme.runwithme.data.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.runwithme.runwithme.model.User

class UserTypeConverter {

    var gson = Gson()

    @TypeConverter
    fun userToString(user: User): String {
        return gson.toJson(user)
    }

    @TypeConverter
    fun stringToUser(data: String): User {
        val type = object : TypeToken<User>() {}.type
        return gson.fromJson(data, type)
    }
}