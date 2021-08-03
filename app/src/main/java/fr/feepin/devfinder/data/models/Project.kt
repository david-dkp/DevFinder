package fr.feepin.devfinder.data.models

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.PropertyName

data class Project(
    @DocumentId val id: String? = null,
    val userId: String,
    val username: String,
    val profilePictureUrl: String,
    val viewCount: Int,
    val title: String,
    val description: String,
    val technologies: List<String>,
    val creationTs: Timestamp,
    val available: Boolean
)
