package com.runwithme.runwithme.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters


@Database(
    entities = [UserEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(UserTypeConverter::class,PhotoTypeConverter::class)
abstract class RunWithMeDatabase :RoomDatabase() {

    abstract fun runWithMeDao() : RunWithMeDao

}