package com.example.reuseit.Application.Models

import com.example.reuseit.Application.Database.Entity.UserEntity

data class UserModel(
    public var Email: String = "",
    public var Password: String = "",
    public var FirstName: String = "",
    public var LastName: String = "",
)
{
    public fun toUserEntity(): UserEntity {
        return UserEntity(email = Email, password = Password, firstName = FirstName, lastName = LastName)
    }

    public fun hashPassword(): String{
        return Password.hashCode().toString()
    }
}