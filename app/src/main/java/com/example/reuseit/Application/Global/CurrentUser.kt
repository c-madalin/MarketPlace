package com.example.reuseit.Application.Global

import com.example.reuseit.Application.Models.UserModel

//Singleton
object CurrentUser {
    public val Data: UserModel = UserModel()
}