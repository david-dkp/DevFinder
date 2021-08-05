package fr.feepin.devfinder.data.models

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.PropertyName
import java.util.*

data class Project(
    @DocumentId val id: String? = null,
    val userId: String = "",
    val username: String = "",
    val profilePictureUrl: String = "",
    val viewCount: Int = -1,
    val title: String = "",
    val description: String = "",
    val technologies: List<String> = Collections.emptyList(),
    val creationTs: Timestamp = Timestamp(0, 0),
    val available: Boolean = false
)
