package fr.feepin.devfinder.ui.login

sealed class LoginState {
    object Loading : LoginState()
    object Success : LoginState()
    class Error(val message: String) : LoginState()
}