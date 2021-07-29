package fr.feepin.devfinder.data.repos

import fr.feepin.devfinder.data.models.Project

interface ProjectRepository {

    suspend fun fetchAllProjects(): List<Project>

    suspend fun fetchProjectsByTechnologies(technologies: List<String>): List<Project>

    suspend fun addProject(userId: String, project: Project)
}