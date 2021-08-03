package fr.feepin.devfinder.ui.chatlist

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import fr.feepin.devfinder.data.repos.ChatRepository
import fr.feepin.devfinder.data.repos.UserRepository
import fr.feepin.devfinder.utils.TimeFormatUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject

@HiltViewModel
class ChatListViewModel @Inject constructor(
    @ApplicationContext val context: Context,
    val userRepository: UserRepository,
    val chatRepository: ChatRepository
) : ViewModel(){

    private val _viewState = MutableLiveData<ChatListViewState>(ChatListViewState(false, Collections.emptyList()))
    val viewState: LiveData<ChatListViewState> = _viewState


    fun fetchChats() {
        userRepository.getUser().value?.let { user ->
            _viewState.value = _viewState.value?.copy(loading = true)

            viewModelScope.launch(Dispatchers.IO) {
                val chats = chatRepository.fetchChatsByIds(user.chatsIdList)

                val newViewItemStates = chats.map {
                    val lastMessage = chatRepository.fetchLastMessageFromChatId(it.id!!)
                    val friendId = if (user.id!! == it.firstUserId) {
                        it.secondUserId
                    } else it.firstUserId

                    val friendUser = userRepository.fetchUserById(friendId)

                    friendUser?.let { friendUser ->
                        val friendStatus = userRepository.fetchUserStatus(friendId)

                        ChatListViewState.ChatItemViewState(
                            it.id!!,
                            friendUser.profilePictureUrl,
                            friendUser.username,
                            lastMessage?.message ?: "",
                            lastMessage?.sendTimeTs?.let { TimeFormatUtils.getFormattedTimestamp(context, it) } ?: "",
                            friendStatus?.online ?: false
                        )
                    }
                }

                withContext(Dispatchers.Main) {
                    _viewState.value = ChatListViewState(
                        false,
                        newViewItemStates.filterNotNull()
                    )
                }
            }
        }
    }
}