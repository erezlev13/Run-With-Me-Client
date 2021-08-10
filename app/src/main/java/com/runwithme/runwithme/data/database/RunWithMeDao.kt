package com.runwithme.runwithme.data.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow


@Dao
interface RunWithMeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(userEntity: UserEntity)

    @Query("SELECT * FROM user_table")
    fun readUserForCoroutine(): Flow<List<UserEntity>>

    @Query("SELECT * FROM user_table")
    suspend fun readUser(): List<UserEntity>

    @Update
    suspend fun updateUser(userEntity: UserEntity)

    @Delete
    suspend fun deleteUser(userEntity: UserEntity)
}