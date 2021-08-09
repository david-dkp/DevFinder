package fr.feepin.devfinder.ui.chat

sealed class ChatEvent {
    data class ShowUserProfile(val userId: String): ChatEvent()
}