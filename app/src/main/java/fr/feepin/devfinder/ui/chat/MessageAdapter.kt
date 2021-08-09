package fr.feepin.devfinder.ui.chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import fr.feepin.devfinder.R
import fr.feepin.devfinder.databinding.ChatFriendMessageItemBinding
import fr.feepin.devfinder.databinding.ChatUserMessageItemBinding

class MessageAdapter(options: FirestoreRecyclerOptions<MessageViewState>, private val onDataChanged: (() -> Unit)? = null) :
    FirestoreRecyclerAdapter<MessageViewState, MessageAdapter.ViewHolder>(
        options
    ) {

    companion object {
        private const val USER_MESSAGE_TYPE = 1
        private const val FRIEND_MESSAGE_TYPE = 2
    }

    override fun getItemViewType(position: Int): Int {
        val messageViewState = getItem(position)
        return if (messageViewState.isUser) USER_MESSAGE_TYPE else FRIEND_MESSAGE_TYPE
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        return if (viewType == USER_MESSAGE_TYPE) {
            ViewHolder.UserMessageViewHolder(
                inflater.inflate(
                    R.layout.chat_user_message_item,
                    parent,
                    false
                )
            )
        } else {
            ViewHolder.FriendMessageViewHolder(
                inflater.inflate(
                    R.layout.chat_friend_message_item,
                    parent,
                    false
                )
            )
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, model: MessageViewState) {
        holder.bind(model)
    }

    override fun onDataChanged() {
        super.onDataChanged()
        onDataChanged?.invoke()
    }

    sealed class ViewHolder(rootView: View) : RecyclerView.ViewHolder(rootView) {

        abstract fun bind(message: MessageViewState)

        class UserMessageViewHolder(rootView: View) : ViewHolder(rootView) {
            private val binding = ChatUserMessageItemBinding.bind(rootView)

            override fun bind(message: MessageViewState) {
                binding.tvMessage.text = message.message
                binding.tvTime.text = message.sendTime
            }

        }

        class FriendMessageViewHolder(rootView: View) : ViewHolder(rootView) {
            private val binding = ChatFriendMessageItemBinding.bind(rootView)

            override fun bind(message: MessageViewState) {
                binding.tvMessage.text = message.message
                binding.tvTime.text = message.sendTime
            }
        }

    }
}