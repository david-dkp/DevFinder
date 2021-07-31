package fr.feepin.devfinder.ui.chat

data class MessageViewState(
    val isUser: Boolean,
    val message: String,
    val sendTime: String
)