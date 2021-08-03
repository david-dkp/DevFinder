package fr.feepin.devfinder.ui.project

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.feepin.devfinder.data.repos.ProjectRepository
import fr.feepin.devfinder.data.repos.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ProjectViewModel @Inject constructor(
    val projectRepository: ProjectRepository
) : ViewModel() {

    private val _viewState = MutableLiveData<ProjectViewState>()
    val viewState: LiveData<ProjectViewState> = _viewState

    fun fetchProject(projectId: String) {
        _viewState.value = ProjectViewState(
            true
        )

        viewModelScope.launch(Dispatchers.IO) {
            val project = projectRepository.fetchProjectById(projectId)

            project?.let { project ->
                withContext(Dispatchers.Main) {
                    _viewState.value = ProjectViewState(false, project)
                }
            }
        }
    }
}