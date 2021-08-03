package fr.feepin.devfinder.data.auth

import java.lang.Exception

sealed class AuthResult {
    data class Success(val uid: String, val newUser: Boolean) : AuthResult()
    data class Failed(val exception: Exception) : AuthResult()
}