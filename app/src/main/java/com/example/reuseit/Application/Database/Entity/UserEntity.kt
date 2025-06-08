package com.example.reuseit.Application.Database.Entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.reuseit.Application.Models.UserModel


@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true) val userID: Long = 0,
    val email: String,
    val password: String,
    val firstName: String,
    val lastName: String,
)
{
    public fun toUserModel(): UserModel{
        return UserModel(
            UserID = userID,
            Email = email,
            Password = password,
            FirstName = firstName,
            LastName = lastName)
    }
}
