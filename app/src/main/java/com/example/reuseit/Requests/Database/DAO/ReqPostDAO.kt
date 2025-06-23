package com.example.reuseit.Requests.Database.DAO

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.reuseit.Requests.Database.Entity.ReqPostEntity

@Dao
interface ReqPostDAO {
    @Query("SELECT * FROM req_posts")
    suspend fun getAllPosts(): List<ReqPostEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPosts(posts: List<ReqPostEntity>)

    @Query("DELETE FROM req_posts")
    suspend fun deleteAllPosts()
}