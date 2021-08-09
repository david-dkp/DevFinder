package fr.feepin.devfinder.data.models

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.PropertyName
import java.util.*

data class Chat(
    @DocumentId val id: String? = null,
    val membersIds: List<String> = Collections.emptyList()
)