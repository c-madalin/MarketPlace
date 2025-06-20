package com.example.reuseit.Application.Database.DAO


import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.reuseit.Application.Database.Entity.PostEntity

@Dao
interface PostDAO {
    @Insert
    suspend fun insert(post: PostEntity): Long

    @Query("SELECT * FROM posts ORDER BY timestamp DESC")
    suspend fun getAllPosts(): List<PostEntity>
}