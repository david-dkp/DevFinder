package fr.feepin.devfinder.data.models

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.PropertyName

data class Chat(
    @DocumentId val id: String? = null,
    val firstUserId: String,
    val secondUserId: String,
)