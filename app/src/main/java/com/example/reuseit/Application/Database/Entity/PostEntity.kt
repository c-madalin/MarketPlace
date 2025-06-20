package com.example.reuseit.Application.Database.Entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "posts")
data class PostEntity(
    @PrimaryKey(autoGenerate = true) val postId: Long = 0,
    val userId: Long,
    val userName: String,
    val title: String,
    val imagePath: String,
    val timestamp: Long = System.currentTimeMillis()
)