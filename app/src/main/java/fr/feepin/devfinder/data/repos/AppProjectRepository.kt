package fr.feepin.devfinder.data.repos

import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import fr.feepin.devfinder.data.models.Project
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppProjectRepository @Inject constructor() : ProjectRepository {

    private val firestore = Firebase.firestore
    private val projectsCollectionRef = firestore.collectionGroup("projects")

    override suspend fun fetchAllProjects(): List<Project> {
        return projectsCollectionRef
            .get()
            .await()
            .toObjects(Project::class.java)
    }

    override suspend fun fetchProjectsByTechnologies(technologies: List<String>): List<Project> {
        return projectsCollectionRef
            .whereArrayContainsAny("technologies", technologies)
            .get()
            .await()
            .toObjects(Project::class.java)
    }

    override suspend fun addProject(userId: String, project: Project) {
        firestore
            .collection("users")
            .document(userId)
            .collection("projects")
            .add(project)
    }

    override suspend fun fetchProjectById(userId: String, projectId: String): Project? {
        return Firebase.firestore.collection("users")
            .document(userId)
            .collection("projects")
            .document(projectId)
            .get()
            .await()
            .toObject(Project::class.java)
    }

    override suspend fun fetchAllProjectsFromUser(userId: String): List<Project> {
        return Firebase.firestore.collection("users")
            .document(userId)
            .collection("projects")
            .get()
            .await()
            .toObjects(Project::class.java)
    }

    override suspend fun incrementProjectViewCount(userId: String, projectId: String) {
        firestore
            .collection("users")
            .document(userId)
            .collection("projects")
            .document(projectId)
            .update("viewCount", FieldValue.increment(1))
            .await()
    }
}