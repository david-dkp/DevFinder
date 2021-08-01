package fr.feepin.devfinder.ui.addproject

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import fr.feepin.devfinder.R
import fr.feepin.devfinder.data.auth.AuthManager
import fr.feepin.devfinder.data.models.Project
import fr.feepin.devfinder.data.repos.ProjectRepository
import fr.feepin.devfinder.data.repos.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@HiltViewModel
class AddProjectViewModel constructor(
    @ApplicationContext val context: Context,
    val userRepository: UserRepository,
    val projectRepository: ProjectRepository
) : ViewModel(){
    private val _viewState: MutableLiveData<AddProjectViewState> = MutableLiveData()
    val viewState: LiveData<AddProjectViewState> = _viewState

    fun addProject(
        title: String,
        description: String
    ) {
        loadingState()

        val cleanTitle = title.trim()
        val cleanDescription = description.trim()

        if (cleanTitle == "") {
            _viewState.value = AddProjectViewState(
                false,
                context.getString(R.string.no_project_title_error_text),
                ""
            )

            return
        }

        authManager.authState.value.uid?.let { uid ->
            viewModelScope.launch(Dispatchers.IO) {
                projectRepository.addProject(
                    uid,
                    Project(
                        null,
                        uid,

                    )
                )
            }
        }

    }

    private fun loadingState() {
        _viewState.value = AddProjectViewState(
            true,
            "",
            "",
            ""
        )
    }

}