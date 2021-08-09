package fr.feepin.devfinder.data.repos

import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import fr.feepin.devfinder.data.models.Chat
import fr.feepin.devfinder.data.models.Message
import kotlinx.coroutines.tasks.await
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppChatRepository @Inject constructor() : ChatRepository {

    private val firestore = Firebase.firestore
    private val chatsCollectionRef = firestore.collection("chats")

    override suspend fun fetchChatsByIds(chatsIdList: List<String>): List<Chat> {
        if (chatsIdList.isEmpty()) {
            return Collections.emptyList()
        }

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
            .orderBy("sendTimeTs", Query.Direction.DESCENDING)
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

    override suspend fun addChat(chat: Chat): String {
        return chatsCollectionRef.add(chat).await().id
    }

    override suspend fun fetchChat(firstUserId: String, secondUserId: String): Chat? {
        val firstUserChats =  chatsCollectionRef
            .whereArrayContains("membersIds", firstUserId)
            .get()
            .await()
            .toObjects(Chat::class.java)

        return firstUserChats.firstOrNull { it.membersIds.contains(secondUserId) }
    }

    override fun getMessagesQuery(chatId: String): Query {
        return chatsCollectionRef
            .document(chatId)
            .collection("messages")
            .orderBy("sendTimeTs", Query.Direction.DESCENDING)
    }

    override suspend fun addMessage(chatId: String, message: Message) {
        chatsCollectionRef
            .document(chatId)
            .collection("messages")
            .add(message)
    }
}