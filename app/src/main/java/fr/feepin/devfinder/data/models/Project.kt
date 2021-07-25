package fr.feepin.devfinder.data.models

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.PropertyName

data class Project(
    @DocumentId val id: String? = null,
    @PropertyName("user_id") val userId: String,
    val username: String,
    @PropertyName("profile_picture_url") val profilePictureUrl: String,
    @PropertyName("view_count") val viewCount: Int,
    val title: String,
    val description: String,
    val technologies: List<String>,
    @PropertyName("creation_ts") val creationTs: Timestamp,
    val available: Boolean
)
