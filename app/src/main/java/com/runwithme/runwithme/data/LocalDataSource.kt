package com.runwithme.runwithme.data

import com.runwithme.runwithme.data.database.RunWithMeDao
import com.runwithme.runwithme.data.database.UserEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    private val runWithMeDao: RunWithMeDao
) {

    fun readUserForCoroutine(): Flow<List<UserEntity>> {
        return runWithMeDao.readUserForCoroutine()
    }

    suspend fun readUser(): List<UserEntity>{
        return runWithMeDao.readUser()
    }

    suspend fun deleteUser(userEntity: UserEntity){
        runWithMeDao.deleteUser(userEntity)
    }
    suspend fun insertUser(userEntity: UserEntity) {
        runWithMeDao.insertUser(userEntity)
    }

    suspend fun updateUser(userEntity: UserEntity){
        runWithMeDao.updateUser(userEntity)
    }



}