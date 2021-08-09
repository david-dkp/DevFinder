package fr.feepin.devfinder.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.feepin.devfinder.data.models.Chat
import fr.feepin.devfinder.data.models.User
import fr.feepin.devfinder.data.repos.ChatRepository
import fr.feepin.devfinder.data.repos.ProjectRepository
import fr.feepin.devfinder.data.repos.UserRepository
import fr.feepin.devfinder.ui.project.ProjectEvent
import fr.feepin.devfinder.utils.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val projectRepository: ProjectRepository,
    private val chatRepository: ChatRepository
) : ViewModel(){

    private val _viewState = MutableLiveData<ProfileViewState>()
    val viewState: LiveData<ProfileViewState> = _viewState

    private val _event = MutableLiveData<Event<ProfileEvent>>()
    val event: LiveData<Event<ProfileEvent>> = _event

    fun fetchUserAndProjects(userId: String) {
        _viewState.value = ProfileViewState(
            ProfileViewState.UserState(true, User(), false),
            ProfileViewState.ProjectsState(true, Collections.emptyList())
        )

        viewModelScope.launch(Dispatchers.IO) {
            val user = userRepository.fetchUserById(userId)
            user?.let { user ->
                withContext(Dispatchers.Main) {
                    _viewState.value = _viewState.value!!.copy(
                        userState = ProfileViewState.UserState(false, user, isSelfUser(user))
                    )
                }
            }
        }

        viewModelScope.launch(Dispatchers.IO) {
            val projects = projectRepository.fetchAllProjectsFromUser(userId)

            withContext(Dispatchers.Main) {
                _viewState.value = _viewState.value!!.copy(
                    projectsState = ProfileViewState.ProjectsState(false, projects)
                )
            }

        }
    }

    private fun isSelfUser(user: User): Boolean {
        return user.id == userRepository.getUser().value?.id
    }

    fun onChatButtonClick() {
        viewState.value?.userState?.user?.id?.let { friendId ->
            viewModelScope.launch(Dispatchers.IO) {
                val userId = userRepository?.getUser().value?.id!!
                val friendId = friendId

                val chat = chatRepository.fetchChat(userId, friendId)

                if (chat == null) {
                    val chatId = chatRepository.addChat(
                        Chat(null, listOf(userId, friendId))
                    )

                    userRepository.addChatIdToUser(userId, chatId)
                    userRepository.addChatIdToUser(friendId, chatId)

                    withContext(Dispatchers.Main) {
                        _event.value = Event(ProfileEvent.EnterChat(chatId))
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        _event.value = Event(ProfileEvent.EnterChat(chat.id!!))
                    }
                }

            }
        }
    }

}