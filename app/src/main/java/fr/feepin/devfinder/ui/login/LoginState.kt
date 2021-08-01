package fr.feepin.devfinder.ui.login

sealed class LoginState {
    object Loading : LoginState()
    class Success(val newUser: Boolean) : LoginState()
    class Error(val message: String) : LoginState()
}