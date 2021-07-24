package fr.feepin.devfinder.ui.login

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import fr.feepin.devfinder.R
import fr.feepin.devfinder.databinding.LoginFragmentBinding

class LoginFragment : Fragment(R.layout.login_fragment){

    private var binding: LoginFragmentBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = LoginFragmentBinding.bind(view)
    }

}