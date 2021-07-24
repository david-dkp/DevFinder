package fr.feepin.devfinder.ui.register

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import fr.feepin.devfinder.R
import fr.feepin.devfinder.databinding.RegisterDescribeFragmentBinding

class RegisterDescribeFragment : Fragment(R.layout.register_describe_fragment){

    private var binding: RegisterDescribeFragmentBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = RegisterDescribeFragmentBinding.bind(view)
    }

}