package fr.feepin.devfinder.data.repos

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import fr.feepin.devfinder.data.models.Project
import fr.feepin.devfinder.data.models.User
import kotlinx.coroutines.tasks.await

interface ProjectRepository {

    //Get all the projects from firestore in the "projects" sub - collections of a "users" document.
    //To do that, you can do a group query
    // More help here https://firebase.google.com/docs/firestore/query-data/queries#collection-group-query

    private val projectCollectionRef get() = Firebase.firestore.collection("projects")
    suspend fun fetchAllProjects(): List<Project>{
        var projectList = arrayListOf<Project>()
        projectCollectionRef.get()
            .addOnSuccessListener { queryDocumentSnapshots ->
                for (snap in queryDocumentSnapshots) {
                    val project = snap.toObject(Project::class.java)
                    projectList.add(project)
                }
            }
        return projectList
    }

    //Here we wanna do collection query and filter query with
    // arrayContainsAny(..)
    suspend fun fetchProjectsByTechnologies(technologies: List<String>): List<Project>

    //Add the project
    suspend fun addProject(userId: String, project: Project){

    }

    suspend fun fetchProjectById(projectId: String): Project?
}