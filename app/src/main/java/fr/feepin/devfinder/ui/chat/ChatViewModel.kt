package fr.feepin.devfinder.ui.chat

import android.content.Context
import android.text.format.DateFormat
import androidx.lifecycle.*
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.firebase.ui.firestore.SnapshotParser
import com.google.firebase.Timestamp
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import fr.feepin.devfinder.data.models.Chat
import fr.feepin.devfinder.data.models.Message
import fr.feepin.devfinder.data.models.User
import fr.feepin.devfinder.data.repos.ChatRepository
import fr.feepin.devfinder.data.repos.UserRepository
import fr.feepin.devfinder.utils.Event
import fr.feepin.devfinder.utils.TimeFormatUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val chatRepository: ChatRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _friendUserLiveData = MutableLiveData<User>()
    val friendUserLiveData: LiveData<User> = _friendUserLiveData

    private val _event = MutableLiveData<Event<ChatEvent>>()
    val event: LiveData<Event<ChatEvent>> = _event

    private var chatId: String? = null

    fun getFirestoreOptions(
        lifeCycleOwner: LifecycleOwner
    ): FirestoreRecyclerOptions<MessageViewState> {
        return FirestoreRecyclerOptions.Builder<MessageViewState>()
            .setLifecycleOwner(lifeCycleOwner)
            .setQuery(chatRepository.getMessagesQuery(chatId!!), getSnapshotParser())
            .build()
    }

    private fun getSnapshotParser(): SnapshotParser<MessageViewState> {
        return SnapshotParser {
            val userId = userRepository.getUser().value!!.id
            val message = it.toObject(Message::class.java)!!

            MessageViewState(
                userId == message.senderUserId,
                message.message,
                TimeFormatUtils.getFormattedTimestamp(context, message.sendTimeTs)
            )
        }
    }


    fun onArgs(args: ChatFragmentArgs) {
        chatId = args.chatId
        fetchChat()
    }

    private fun fetchChat() {
        viewModelScope.launch(Dispatchers.IO) {
            val chat = chatRepository.fetchChatById(chatId!!)

            val userId = userRepository.getUser().value?.id

            userId?.let {
                chat?.let {
                    val friendUserId = if (userId == it.membersIds[0]) {
                        it.membersIds[1]
                    } else it.membersIds[0]

                    val friendUser = userRepository.fetchUserById(friendUserId)

                    friendUser?.let {
                        withContext(Dispatchers.Main) {
                            _friendUserLiveData.value = it
                        }
                    }

                }
            }

        }
    }

    fun onSendMessage(message: String) {
        val cleanMessage = message.trim()
            .replace("\\s+", "")

        if (cleanMessage.isEmpty()) return

        viewModelScope.launch(Dispatchers.IO) {
            chatRepository.addMessage(
                chatId!!,
                Message(
                    senderUserId = userRepository.getUser().value?.id!!,
                    message = cleanMessage,
                    sendTimeTs = Timestamp.now()
                )
            )
        }
    }

    fun onUserClick() {
        friendUserLiveData.value?.let {
            _event.value = Event(ChatEvent.ShowUserProfile(it.id!!))
        }
    }

}