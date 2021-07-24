package fr.feepin.devfinder.ui.register

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import fr.feepin.devfinder.R
import fr.feepin.devfinder.databinding.RegisterTechnologiesFragmentBinding

class RegisterTechnologiesFragment : Fragment(R.layout.register_technologies_fragment){

    private var binding: RegisterTechnologiesFragmentBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = RegisterTechnologiesFragmentBinding.bind(view)
    }

}