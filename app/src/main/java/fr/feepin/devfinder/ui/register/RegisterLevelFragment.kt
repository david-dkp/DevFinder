package fr.feepin.devfinder.ui.register

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import fr.feepin.devfinder.R
import fr.feepin.devfinder.databinding.RegisterLevelFragmentBinding

class RegisterLevelFragment : Fragment(R.layout.register_level_fragment){

    private var binding: RegisterLevelFragmentBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = RegisterLevelFragmentBinding.bind(view)
    }

}