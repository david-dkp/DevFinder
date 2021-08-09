package fr.feepin.devfinder.ui.profile

import fr.feepin.devfinder.data.models.Project
import fr.feepin.devfinder.data.models.User

data class ProfileViewState(
    val userState: UserState,
    val projectsState: ProjectsState
) {
    data class UserState(val loading: Boolean, val user: User, val isSelfUser: Boolean)
    data class ProjectsState(val loading: Boolean, val projects: List<Project>)
}
