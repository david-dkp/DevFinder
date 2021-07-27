package fr.feepin.devfinder.data.auth

import java.lang.Exception

sealed class AuthResult {
    class Success(val uid: String, val newUser: Boolean) : AuthResult()
    class Failed(exception: Exception) : AuthResult()
}