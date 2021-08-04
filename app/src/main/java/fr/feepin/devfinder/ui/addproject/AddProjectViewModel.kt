package fr.feepin.devfinder.ui.addproject

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Timestamp
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import fr.feepin.devfinder.R
import fr.feepin.devfinder.data.auth.AuthManager
import fr.feepin.devfinder.data.models.Project
import fr.feepin.devfinder.data.models.User
import fr.feepin.devfinder.data.repos.ProjectRepository
import fr.feepin.devfinder.data.repos.UserRepository
import fr.feepin.devfinder.utils.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class AddProjectViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val userRepository: UserRepository,
    private val projectRepository: ProjectRepository
) : ViewModel() {

    private val _viewState: MutableLiveData<AddProjectViewState> = MutableLiveData()
    val viewState: LiveData<AddProjectViewState> = _viewState

    private val _event = MutableLiveData<Event<AddProjectEvent>>()
    val event: LiveData<Event<AddProjectEvent>> = _event

    fun onConfirmClick(
        title: String,
        description: String,
        technologiesText: String
    ) {

        _viewState.value = AddProjectViewState(true, "")

        userRepository.getUser().value?.let { user ->
            viewModelScope.launch(Dispatchers.IO) {

                val cleanTitle = title.trim()
                val cleanDescription = description.trim()
                val technologies = technologiesText.split("1")
                    .map { it.trim() }
                    .filter { it.isNotEmpty() }

                if (!validateInputs(cleanTitle, cleanDescription, technologies)) return@launch

                addProject(user, cleanTitle, cleanDescription, technologies)

            }
        }
    }

    private suspend fun validateInputs(
        title: String,
        description: String,
        technologies: List<String>
    ): Boolean {
        if (title == "") {
            withContext(Dispatchers.Main) {
                _viewState.value = AddProjectViewState(
                    false,
                    context.getString(R.string.no_project_title_error_text)
                )
            }
            return false
        }
        return true
    }

    private suspend fun addProject(user: User, title: String, description: String, technologies: List<String>) {
        projectRepository.addProject(
            user.id!!,
            Project(
                null,
                user.id!!,
                user.username,
                user.profilePictureUrl,
                0,
                title,
                description,
                technologies,
                Timestamp.now(),
                true
            )
        )

        _viewState.value = AddProjectViewState(false, "")
        _event.value = Event(AddProjectEvent.ProjectAdded)
    }

}