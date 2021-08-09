package fr.feepin.devfinder.ui.project

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.feepin.devfinder.data.auth.AuthManager
import fr.feepin.devfinder.data.models.Chat
import fr.feepin.devfinder.data.models.Project
import fr.feepin.devfinder.data.repos.ChatRepository
import fr.feepin.devfinder.data.repos.ProjectRepository
import fr.feepin.devfinder.data.repos.UserRepository
import fr.feepin.devfinder.utils.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ProjectViewModel @Inject constructor(
    private val authManager: AuthManager,
    private val userRepository: UserRepository,
    private val projectRepository: ProjectRepository,
    private val chatRepository: ChatRepository
) : ViewModel() {

    private val _viewState = MutableLiveData<ProjectViewState>()
    val viewState: LiveData<ProjectViewState> = _viewState

    private val _event = MutableLiveData<Event<ProjectEvent>>()
    val event: LiveData<Event<ProjectEvent>> = _event

    fun fetchProject(userId: String, projectId: String) {
        _viewState.value = ProjectViewState(
            true,
            null,
            false
        )

        viewModelScope.launch(Dispatchers.IO) {
            val project = projectRepository.fetchProjectById(userId, projectId)

            project?.let { project ->
                withContext(Dispatchers.Main) {
                    _viewState.value = ProjectViewState(false, project, isUserProject(project))
                }

                projectRepository.incrementProjectViewCount(project.userId, project.id!!)
            }
        }
    }

    private fun isUserProject(project: Project): Boolean {
        return project.userId == authManager.authState.value.uid
    }

    fun onStartConversionClick() {
        viewState.value?.project?.let {
            viewModelScope.launch(Dispatchers.IO) {
                val userId = authManager?.authState?.value?.uid!!
                val friendId = viewState?.value?.project?.userId!!

                val chat = chatRepository.fetchChat(userId, friendId)

                if (chat == null) {
                    val chatId = chatRepository.addChat(
                        Chat(null, listOf(userId, friendId))
                    )

                    userRepository.addChatIdToUser(userId, chatId)
                    userRepository.addChatIdToUser(friendId, chatId)

                    withContext(Dispatchers.Main) {
                        _event.value = Event(ProjectEvent.EnterChat(chatId))
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        _event.value = Event(ProjectEvent.EnterChat(chat.id!!))
                    }
                }

            }
        }
    }
}