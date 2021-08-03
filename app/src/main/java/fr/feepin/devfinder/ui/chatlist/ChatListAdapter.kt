package fr.feepin.devfinder.ui.chatlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import fr.feepin.devfinder.R
import fr.feepin.devfinder.data.models.Chat
import fr.feepin.devfinder.databinding.ChatListItemBinding

class ChatListAdapter(val onChatClick: (String) -> Unit) : ListAdapter<ChatListViewState.ChatItemViewState, ChatListAdapter.ViewHolder>(
    DIFF_CALLBACK) {

    companion object {
        val DIFF_CALLBACK = object: DiffUtil.ItemCallback<ChatListViewState.ChatItemViewState>() {
            override fun areItemsTheSame(
                oldItem: ChatListViewState.ChatItemViewState,
                newItem: ChatListViewState.ChatItemViewState
            ): Boolean {
                return oldItem.chatId == newItem.chatId
            }

            override fun areContentsTheSame(
                oldItem: ChatListViewState.ChatItemViewState,
                newItem: ChatListViewState.ChatItemViewState
            ): Boolean {
                return oldItem == newItem
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        return ViewHolder(inflater.inflate(R.layout.chat_list_item, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(rootView: View) : RecyclerView.ViewHolder(rootView) {
        private val binding = ChatListItemBinding.bind(rootView)

        init {
            binding.root.setOnClickListener {
                onChatClick(getItem(bindingAdapterPosition).chatId)
            }
        }

        fun bind(chatListItem: ChatListViewState.ChatItemViewState) {
            binding.apply {
                Glide.with(binding.root)
                    .load(chatListItem.photoUrl)
                    .into(ivProfilePicture)

                tvName.text = chatListItem.username
                tvLastMessage.text = chatListItem.lastMessage
                tvLastTimeMessage.text = chatListItem.lastMessageTime
                vOnlineIndicator.isVisible = chatListItem.online
            }
        }

    }
}