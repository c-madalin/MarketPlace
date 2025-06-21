package com.example.reuseit.Application.Database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.reuseit.Application.Database.DAO.UserDAO
import com.example.reuseit.Application.Database.Entity.UserEntity
import com.example.reuseit.Application.Database.Entity.PostEntity
import com.example.reuseit.Application.Database.DAO.PostDAO



@Database(entities = [UserEntity::class, PostEntity::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDAO(): UserDAO
    abstract fun postDAO(): PostDAO
}