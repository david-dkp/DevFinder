package fr.feepin.devfinder.data.models

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.PropertyName
import java.sql.Timestamp

data class User(
    @DocumentId val id: String? = null,
    val level: Int,
    @PropertyName("profile_picture_url") val profilePictureUrl: String,
    val technologies: List<String>,
    val username: String,
    val bio: String,
    @PropertyName("creation_ts") val creationTs: Timestamp
)
