package fr.feepin.devfinder.ui.project

sealed class ProjectEvent {
    data class EnterChat(val chatId: String): ProjectEvent()
}