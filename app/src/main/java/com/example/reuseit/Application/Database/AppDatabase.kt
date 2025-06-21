package com.example.reuseit.Application.Database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.reuseit.Application.Database.DAO.CommentDAO
import com.example.reuseit.Application.Database.DAO.UserDAO
import com.example.reuseit.Application.Database.Entity.UserEntity
import com.example.reuseit.Application.Database.Entity.PostEntity
import com.example.reuseit.Application.Database.DAO.PostDAO
import com.example.reuseit.Application.Database.Entity.CommentEntity


//
//@Database(entities = [UserEntity::class, PostEntity::class], version = 2)
//abstract class AppDatabase : RoomDatabase() {
//    abstract fun userDAO(): UserDAO
//    abstract fun postDAO(): PostDAO
//}


@Database(
    entities = [UserEntity::class, PostEntity::class, CommentEntity::class],
    version = 3
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDAO(): UserDAO
    abstract fun postDAO(): PostDAO
    abstract fun commentDAO(): CommentDAO
}
