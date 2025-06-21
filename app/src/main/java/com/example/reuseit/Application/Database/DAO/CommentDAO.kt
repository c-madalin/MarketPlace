package com.example.reuseit.Application.Database.DAO

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.reuseit.Application.Database.Entity.CommentEntity

@Dao
interface CommentDAO {
    @Insert
    suspend fun insert(comment: CommentEntity): Long

    @Query("SELECT * FROM comments WHERE postId = :postId ORDER BY timestamp ASC")
    suspend fun getCommentsForPost(postId: Long): List<CommentEntity>
}
