package fr.feepin.devfinder.ui.projectlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.feepin.devfinder.data.auth.AuthManager
import fr.feepin.devfinder.data.models.Project
import fr.feepin.devfinder.data.repos.ProjectRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ProjectListViewModel @Inject constructor(
    private val projectRepository: ProjectRepository
) : ViewModel(){

    private val _viewStateLiveData: MutableLiveData<ProjectListViewState> = MutableLiveData(
        ProjectListViewState(emptyList(), true)
    )
    val viewState: LiveData<ProjectListViewState> = _viewStateLiveData

    fun fetchProjects() {
        _viewStateLiveData.value = _viewStateLiveData.value?.copy(isLoading = true)
        viewModelScope.launch(Dispatchers.IO) {
            val projects = projectRepository.fetchAllProjects()
            withContext(Dispatchers.Main) {
                _viewStateLiveData.value = ProjectListViewState(projects, false)
            }
        }
    }

}