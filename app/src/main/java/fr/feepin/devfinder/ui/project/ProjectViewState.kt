package fr.feepin.devfinder.ui.project

import fr.feepin.devfinder.data.models.Project

data class ProjectViewState(
    val loading: Boolean,
    val project: Project? = null,
    val userProject: Boolean,
)