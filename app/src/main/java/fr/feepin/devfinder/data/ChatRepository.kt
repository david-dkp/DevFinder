package fr.feepin.devfinder.data

import androidx.paging.PagingSource
import com.google.firebase.Timestamp
import fr.feepin.devfinder.data.models.Chat
import fr.feepin.devfinder.data.models.Message

interface ChatRepository {
    suspend fun fetchChatsForUserId(userId: String): List<Chat>
    suspend fun fetchLastMessageFromChatId(chatId: String): Message
    suspend fun fetchRecentMessages(chatId: String, searchFromTs: Timestamp): List<Message>
    fun listenToNewMessage(chatId: String): Message
    suspend fun addMessage(chatId: String, message: Message)
    suspend fun addChat(chat: Chat)
}