package com.example.reuseit.Requests.Database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.reuseit.Application.Database.Entity.UserEntity
import com.example.reuseit.Requests.Database.DAO.ReqPostDAO
import com.example.reuseit.Requests.Database.DAO.ReqUserDAO
import com.example.reuseit.Requests.Database.Entity.ReqPostEntity
import com.example.reuseit.Requests.Database.Entity.ReqUserEntity

@Database(entities = [ReqUserEntity::class, ReqPostEntity::class], version = 1)
abstract class ReqAppDatabase: RoomDatabase() {
    public abstract fun reqUserDAO(): ReqUserDAO
    public abstract fun reqPostDAO(): ReqPostDAO
}