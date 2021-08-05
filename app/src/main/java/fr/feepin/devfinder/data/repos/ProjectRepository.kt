package fr.feepin.devfinder.data.repos

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import fr.feepin.devfinder.data.models.Project
import fr.feepin.devfinder.data.models.User
import kotlinx.coroutines.tasks.await

interface ProjectRepository {

    suspend fun fetchAllProjects(): List<Project>

    suspend fun fetchProjectsByTechnologies(technologies: List<String>): List<Project>

    suspend fun addProject(userId: String, project: Project)

    suspend fun fetchProjectById(userId: String, projectId: String): Project?

    suspend fun incrementProjectViewCount(userId: String, projectId: String)

}