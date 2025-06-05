package com.example.reuseit.Application.Database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.reuseit.Application.Database.DAO.UserDAO
import com.example.reuseit.Application.Database.Entity.UserEntity


@Database(entities = [UserEntity::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    public abstract fun userDAO(): UserDAO
}