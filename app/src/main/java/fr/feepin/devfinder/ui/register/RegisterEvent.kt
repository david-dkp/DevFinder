package fr.feepin.devfinder.ui.register

sealed class RegisterEvent {
    object RegistrationSucceeded : RegisterEvent()
    object RegistrationFailed : RegisterEvent()
    object RegistrationCancelled : RegisterEvent()
}