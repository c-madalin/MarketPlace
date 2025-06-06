package com.example.reuseit.Utils

import android.util.Log

object ValidationUtils {
    fun isValidEmail(email: String): Boolean {
        return true//regex or something for validation
    }

    fun isValidPassword(password: String): Boolean {
        //return true // just for test

        Log.d("Password", password)
        Log.d("PasswordL", password.length.toString())
        Log.d("PasswordD", password.any { it.isDigit() }.toString())
        Log.d("PasswordL", password.any { it.isLetter()}.toString())

        return  password.length >= 5 &&
                password.any { it.isDigit() } &&
                password.any { it.isLetter() }
    }

    fun hashPassword(password: String): String {
        return password //for hashing
    }

    fun getPasswordStrength(password: String): PasswordStrength {
        //return PasswordStrength.STRONG //just for test

        if (password.length < 5) return PasswordStrength.WEAK
        
        var score = 0
        if (password.any { it.isDigit() }) score++
        if (password.any { it.isUpperCase() }) score++
        if (password.any { it.isLowerCase() }) score++
        if (password.any { !it.isLetterOrDigit() }) score++
        
        return when (score) {
            0, 1 -> PasswordStrength.WEAK
            2 -> PasswordStrength.MEDIUM
            else -> PasswordStrength.STRONG
        }
    }
}

enum class PasswordStrength {
    WEAK, MEDIUM, STRONG
} 