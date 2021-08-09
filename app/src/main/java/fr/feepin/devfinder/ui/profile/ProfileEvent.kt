package fr.feepin.devfinder.ui.profile

sealed class ProfileEvent {
    data class EnterChat(val chatId: String): ProfileEvent()
}
