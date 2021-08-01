package fr.feepin.devfinder.ui.projectlist

import fr.feepin.devfinder.data.models.Project

data class ProjectListViewState(
    val projects: List<Project>,
    val isLoading: Boolean
)