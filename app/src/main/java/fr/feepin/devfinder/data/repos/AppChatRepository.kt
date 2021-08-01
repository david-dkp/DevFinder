package fr.feepin.devfinder.data.repos

import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import fr.feepin.devfinder.data.models.Chat
import fr.feepin.devfinder.data.models.Message
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppChatRepository @Inject constructor() : ChatRepository {

    private val firestore = Firebase.firestore
    private val chatsCollectionRef = firestore.collection("chats")

    override suspend fun fetchChatsByIds(chatsIdList: List<String>): List<Chat> {
        return chatsCollectionRef
            .whereIn(FieldPath.documentId(), chatsIdList)
            .get()
            .await()
            .toObjects(Chat::class.java)
    }

    override suspend fun fetchLastMessageFromChatId(chatId: String): Message? {
        return chatsCollectionRef
            .document(chatId)
            .collection("messages")
            .orderBy("send_time_ts", Query.Direction.DESCENDING)
            .limit(1)
            .get()
            .await()
            .toObjects(Message::class.java)
            .firstOrNull()
    }

    override suspend fun fetchChatById(chatId: String): Chat? {
        return chatsCollectionRef
            .document(chatId)
            .get()
            .await()
            .toObject(Chat::class.java)
    }

    override suspend fun addChat(chat: Chat) {
        chatsCollectionRef.add(chat).await()
    }

    override fun getMessagesQuery(chatId: String): Query {
        return chatsCollectionRef
            .document(chatId)
            .collection("messages")
            .orderBy("send_time_ts", Query.Direction.DESCENDING)
            .limit(50)
    }

    override suspend fun addMessage(chatId: String, message: Message) {
        chatsCollectionRef
            .document(chatId)
            .collection("messages")
            .add(message)
    }
}