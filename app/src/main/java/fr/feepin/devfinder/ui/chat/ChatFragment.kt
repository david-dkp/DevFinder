package fr.feepin.devfinder.ui.chat

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import fr.feepin.devfinder.R
import fr.feepin.devfinder.data.models.User
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

        binding?.toolbar?.setupWithNavController(findNavController())

        setupStatesListeners()
        setupChatList()
        setupClickListeners()
    }

    private fun setupStatesListeners() {
        viewModel.friendUserLiveData.observe(viewLifecycleOwner) { user ->
            renderUser(user)
        }

        viewModel.event.observe(viewLifecycleOwner) {
            it.getData()?.let {
                renderEvent(it)
            }
        }
    }

    private fun renderUser(user: User) {
        binding?.apply {
            Glide.with(requireContext())
                .load(user.profilePictureUrl)
                .into(ivFriendProfilePicture)
            tvFriendUsername.text = user.username
        }
    }

    private fun renderEvent(event: ChatEvent) {
        if (event is ChatEvent.ShowUserProfile) {
            findNavController().navigate(
                ChatFragmentDirections.actionToProfileFragment(event.userId)
            )
        }
    }

    private fun setupChatList() {
        binding?.rvChatMessages?.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, true)
            adapter = MessageAdapter(viewModel.getFirestoreOptions(viewLifecycleOwner)) {
                smoothScrollToPosition(0)
            }

        }
    }

    private fun setupClickListeners() {
        binding?.apply {
            btnSend.setOnClickListener {
                viewModel.onSendMessage(etMessage.text.toString())
                clearMessageInput()
            }

            val onUserClick: (View) -> Unit = {
                viewModel.onUserClick()
            }

            ivFriendProfilePicture.setOnClickListener(onUserClick)
            tvFriendUsername.setOnClickListener(onUserClick)
        }
    }

    private fun clearMessageInput() {
        binding?.etMessage?.setText("")
    }

}