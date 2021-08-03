package fr.feepin.devfinder.data.models

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.PropertyName

data class Message(
    @DocumentId val id: String? = null,
    val senderUserId: String,
    val receiverUserId: String,
    val message: String,
    val sendTimeTs: Timestamp,
)
