package com.example.reuseit.Requests.Database.Entity

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "req_posts")
data class ReqPostEntity(
    @PrimaryKey(autoGenerate = true) val reqPostId: Long = 0,
    val userID: Long,
    val postTitle: String,
    val postMessage: String
)
