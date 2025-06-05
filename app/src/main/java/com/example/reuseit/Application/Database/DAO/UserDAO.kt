package com.example.reuseit.Application.Database.DAO

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.reuseit.Application.Database.Entity.UserEntity

@Dao
interface UserDAO {
    @Query("SELECT UserID FROM users WHERE email = :email AND password = :password")
    suspend fun GetUserID(email: String, password: String): Int

    @Insert
    suspend fun InsertUser(user: UserEntity)

    @Query("SELECT UserID FROM users WHERE email = :email")
    suspend fun CheckIfUserExists(email: String): Int
}