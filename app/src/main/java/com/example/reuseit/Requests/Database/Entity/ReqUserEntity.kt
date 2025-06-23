package com.example.reuseit.Requests.Database.Entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "req_users")
data class ReqUserEntity(
    @PrimaryKey(autoGenerate = true) val reqUserId: Long = 0,
    val userName: String,
    val userAddress: String
)
