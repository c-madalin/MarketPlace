package com.example.reuseit.Application.Global

import com.example.reuseit.Application.Models.UserModel

//Singleton
object CurrentUser {
    public val Data: UserModel = UserModel()
    public fun Clear() {
        Data.UserID = -1
        Data.Email = ""
        Data.Password = ""
        Data.FirstName = ""
        Data.LastName = ""
    }
}