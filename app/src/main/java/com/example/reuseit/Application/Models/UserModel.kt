package com.example.reuseit.Application.Models

import com.example.reuseit.Application.Database.Entity.UserEntity
import com.example.reuseit.Application.Utils.ValidationUtils
import com.example.reuseit.Application.Utils.PasswordStrength

data class UserModel(
    public var UserID: Long = -1,
    public var Email: String = "",
    public var Password: String = "",
    public var FirstName: String = "",
    public var LastName: String = "",
)
{
    public fun toUserEntity(): UserEntity {
        return UserEntity(
            email = Email,
            password = ValidationUtils.hashPassword(Password),
            firstName = FirstName,
            lastName = LastName
        )
    }

    public fun validateEmail(): Boolean {
        return ValidationUtils.isValidEmail(Email)
    }

    public fun validatePassword(): Boolean {
        return ValidationUtils.isValidPassword(Password)
    }

    public fun getPasswordStrength(): PasswordStrength {
        return ValidationUtils.getPasswordStrength(Password)
    }

    public fun validateName(): Boolean {
        return FirstName.isNotBlank() && LastName.isNotBlank() &&
               FirstName.length >= 2 && LastName.length >= 2
    }
}