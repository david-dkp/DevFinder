package fr.feepin.devfinder.data.repos

import androidx.paging.PagingSource
import com.google.firebase.Timestamp
import com.google.firebase.firestore.Query
import fr.feepin.devfinder.data.models.Chat
import fr.feepin.devfinder.data.models.Message

interface ChatRepository {
    suspend fun fetchChatsByIds(chatsIdList: List<String>): List<Chat>
    suspend fun fetchLastMessageFromChatId(chatId: String): Message?
    suspend fun fetchChatById(chatId: String): Chat?

    suspend fun addChat(chat: Chat)

    fun getMessagesQuery(chatId: String): Query
    suspend fun addMessage(chatId: String, message: Message)
}