package fr.feepin.devfinder.ui.chat

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import fr.feepin.devfinder.R
import fr.feepin.devfinder.databinding.ChatFragmentBinding

@AndroidEntryPoint
class ChatFragment : Fragment(R.layout.chat_fragment) {

    private val args: ChatFragmentArgs by navArgs()

    private var binding: ChatFragmentBinding? = null
    private val viewModel: ChatViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = ChatFragmentBinding.bind(view)
        viewModel.onArgs(args)

        setupFriendUser()
        setupChatList()
    }

    private fun setupFriendUser() {
        viewModel.friendUserLiveData.observe(viewLifecycleOwner) { user ->
            binding?.apply {
                tvFriendUsername.text = user.username
                Glide
                    .with(requireContext())
                    .load(user.profilePictureUrl)
                    .into(ivFriendProfilePicture)
            }
        }
    }

    private fun setupChatList() {
        binding?.rvChatMessages?.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, true)
            adapter = MessageAdapter(viewModel.getFirestoreOptions(viewLifecycleOwner))
        }
    }

}