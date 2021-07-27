package fr.feepin.devfinder.data

import fr.feepin.devfinder.data.models.Project

interface ProjectRepository {

    //Get all the Projects from firestore in the "projects" sub-collection of a "users" document.
    //To do that, you can do a group collection query
    //More help here: https://firebase.google.com/docs/firestore/query-data/queries#collection-group-query
    suspend fun fetchAllProjects(): List<Project>

    //Here again, we do a group collection query and then a filter query with
    // arrayContainsAny(...)
    suspend fun fetchProjectsByTechnologies(technologies: List<String>): List<Project>

    //Add Project the th
    suspend fun addProject(project: Project)
}