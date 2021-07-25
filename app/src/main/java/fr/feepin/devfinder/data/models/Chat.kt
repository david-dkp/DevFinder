package fr.feepin.devfinder.data.models

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.PropertyName

data class Chat(
    @DocumentId val id: String,
    @PropertyName("first_user_id") val firstUserId: String,
    @PropertyName("second_user_id") val secondUserId: String,
)