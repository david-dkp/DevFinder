package fr.feepin.devfinder.ui.chatlist

data class ChatListViewState(
    val loading: Boolean,
    val chatListItemViewStates: List<ChatItemViewState>
) {
    data class ChatItemViewState(
        val chatId: String,
        val photoUrl: String,
        val username: String,
        val lastMessage: String,
        val lastMessageTime: String,
        val online: Boolean,
    )
}
