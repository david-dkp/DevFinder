package fr.feepin.devfinder.data.models

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId

data class Status(
    @DocumentId val id: String? = null,
    val online: Boolean,
    val lastTimeOnlineTs: Timestamp
)