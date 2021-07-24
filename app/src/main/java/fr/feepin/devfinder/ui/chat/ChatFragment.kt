package fr.feepin.devfinder.ui.chat

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import fr.feepin.devfinder.R
import fr.feepin.devfinder.databinding.ChatFragmentBinding

class ChatFragment : Fragment(R.layout.chat_fragment) {

    private var binding: ChatFragmentBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = ChatFragmentBinding.bind(view)
    }

}