package fr.feepin.devfinder.data.auth

sealed class AuthState(val uid: String? = null, val profileImageUrl: String? = null) {
    object Unauthenticated : AuthState()
    object Authenticating : AuthState()
    class Authenticated(uid: String, profileImageUrl: String) : AuthState(uid, profileImageUrl)
}