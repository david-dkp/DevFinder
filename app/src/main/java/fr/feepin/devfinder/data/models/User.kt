package fr.feepin.devfinder.data.models

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.PropertyName
import java.util.*

data class User(
    @DocumentId val id: String? = null,
    val level: Int = Level.BEGINNER.level,
    val profilePictureUrl: String = "",
    val technologies: List<String> = Collections.emptyList(),
    val username: String = "Anonymous",
    val bio: String = "",
    val creationTs: Timestamp? = null,
    val chatsIdList: List<String> = Collections.emptyList()
)
