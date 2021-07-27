package fr.feepin.devfinder.data.auth

sealed class AuthState {
    object Unauthenticated : AuthState()
    object Authenticating : AuthState()
    class Authenticated(uid: String) : AuthState()
}