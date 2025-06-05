package com.example.reuseit.Application.Database.Entity

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true) val userID: Int = 0,
    val email: String,
    val password: String,
    val firstName: String,
    val lastName: String,
)
