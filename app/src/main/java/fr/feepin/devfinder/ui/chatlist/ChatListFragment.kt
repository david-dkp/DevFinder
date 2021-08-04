package fr.feepin.devfinder.ui.chatlist

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import fr.feepin.devfinder.R
import fr.feepin.devfinder.databinding.ChatListFragmentBinding

@AndroidEntryPoint
class ChatListFragment : Fragment(R.layout.chat_list_fragment) {

    private val viewModel: ChatListViewModel by viewModels()

    private var binding: ChatListFragmentBinding? = null

    private var adapter: ChatListAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = ChatListFragmentBinding.bind(view)

        adapter = ChatListAdapter { chatId ->
            findNavController().navigate(
                ChatListFragmentDirections.actionToChatFragment(chatId)
            )
        }

        binding?.rvChats?.apply {
            adapter = adapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        viewModel.viewState.observe(viewLifecycleOwner) {
            renderUi(it)
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.fetchChats()
    }

    private fun renderUi(viewState: ChatListViewState) {
        //Handle loading

        adapter?.submitList(viewState.chatListItemViewStates)
    }

}