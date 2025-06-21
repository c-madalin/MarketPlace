package com.example.reuseit.Application.Database.Entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "comments")
data class CommentEntity(
    @PrimaryKey(autoGenerate = true) val commentId: Long = 0,
    val postId: Long,
    val userId: Long,
    val userName: String,
    val content: String,
    val timestamp: Long = System.currentTimeMillis()
)
