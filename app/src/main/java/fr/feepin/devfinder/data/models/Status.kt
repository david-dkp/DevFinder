package fr.feepin.devfinder.data.models

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.PropertyName

data class Status(
    @DocumentId val id: String? = null,
    val online: Boolean,
    @PropertyName("last_time_online_ts") val lastTimeOnlineTs: Timestamp
)