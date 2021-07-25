package fr.feepin.devfinder.data.models

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.PropertyName

data class Message(
    @DocumentId val id: String,
    @PropertyName("sender_user_id") val senderUserId: String,
    @PropertyName("receiver_user_id") val receiverUserId: String,
    val message: String,
    @PropertyName("send_time_ts") val sendTimeTs: Timestamp,
)
