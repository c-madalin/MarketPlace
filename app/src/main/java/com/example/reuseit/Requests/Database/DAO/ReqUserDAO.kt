package com.example.reuseit.Requests.Database.DAO

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.reuseit.Requests.Database.Entity.ReqUserEntity

@Dao
interface ReqUserDAO {
    @Query("SELECT * FROM req_users")
    suspend fun getAllUsers(): List<ReqUserEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsers(users: List<ReqUserEntity>)

    @Query("DELETE FROM req_users")
    suspend fun deleteAllUsers()
}