package fr.feepin.devfinder.ui.register.technologies

import android.os.Bundle
import android.text.InputType
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ImageSpan
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.chip.ChipDrawable
import dagger.hilt.android.AndroidEntryPoint
import fr.feepin.devfinder.R
import fr.feepin.devfinder.databinding.RegisterTechnologiesFragmentBinding
import fr.feepin.devfinder.ui.register.RegisterViewModel

@AndroidEntryPoint
class RegisterTechnologiesFragment : Fragment(R.layout.register_technologies_fragment){

    private var binding: RegisterTechnologiesFragmentBinding? = null

    private val registerViewModel: RegisterViewModel by hiltNavGraphViewModels(R.id.register_graph)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = RegisterTechnologiesFragmentBinding.bind(view)

        setupTechnologiesInput()
    }

    private fun setupTechnologiesInput() {
        binding?.apply {
            includeBtnNext.btnNext.setOnClickListener {
                registerViewModel.onTechnologies(
                    inputTechnologies.editText!!.text.toString()
                        .split(",")
                        .map { it.trim() }
                        .filter { it != "" }
                )

                findNavController().navigate(
                    RegisterTechnologiesFragmentDirections.actionRegisterTechnologiesFragmentToRegisterLevelFragment()
                )
            }
        }
    }

}