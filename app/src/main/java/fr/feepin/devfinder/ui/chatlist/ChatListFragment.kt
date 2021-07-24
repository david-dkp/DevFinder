package fr.feepin.devfinder.ui.chatlist

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import fr.feepin.devfinder.R
import fr.feepin.devfinder.databinding.ChatListItemBinding

class ChatListFragment : Fragment(R.layout.chat_list_fragment) {

    private var binding: ChatListItemBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = ChatListItemBinding.bind(view)
    }

}