package fr.feepin.devfinder.ui.chat

import android.content.Context
import android.text.format.DateFormat
import androidx.lifecycle.*
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.firebase.ui.firestore.SnapshotParser
import com.google.firebase.Timestamp
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import fr.feepin.devfinder.data.models.Message
import fr.feepin.devfinder.data.models.User
import fr.feepin.devfinder.data.repos.ChatRepository
import fr.feepin.devfinder.data.repos.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@HiltViewModel
class ChatViewModel(
    @ApplicationContext private val context: Context,
    private val chatRepository: ChatRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private var chatId: String? = null

    private val _friendUserLiveData = MutableLiveData<User>()
    val friendUserLiveData: LiveData<User> = _friendUserLiveData

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
            val userId = userRepository.listenUser().value.id!!
            val message = it.toObject(Message::class.java)!!

            MessageViewState(
                userId == message.senderUserId,
                message.message,
                getFormattedTimestamp(message.sendTimeTs)
            )
        }
    }

    private fun getFormattedTimestamp(timestamp: Timestamp): String {
        val timeFormat = DateFormat.getTimeFormat(context)

        return timeFormat.format(timestamp.toDate())
    }

    fun onArgs(args: ChatFragmentArgs) {
        chatId = args.chatId

        viewModelScope.launch(Dispatchers.IO) {
            val chat = chatRepository.fetchChatById(args.chatId)
            val userId = userRepository.listenUser().value.id!!

            val friendUserId = if (chat.firstUserId == userId) {
                chat.secondUserId
            } else {
                chat.firstUserId
            }

            val friendUser = userRepository.fetchUserById(friendUserId)

            withContext(Dispatchers.Main) {
                _friendUserLiveData.value = friendUser
            }

        }

    }

}