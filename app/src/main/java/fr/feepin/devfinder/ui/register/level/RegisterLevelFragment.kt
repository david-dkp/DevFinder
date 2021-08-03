package fr.feepin.devfinder.ui.register.level

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import fr.feepin.devfinder.R
import fr.feepin.devfinder.data.models.Level
import fr.feepin.devfinder.databinding.RegisterLevelFragmentBinding
import fr.feepin.devfinder.ui.register.RegisterEvent
import fr.feepin.devfinder.ui.register.RegisterViewModel
import fr.feepin.devfinder.ui.register.RegisterViewState

@AndroidEntryPoint
class RegisterLevelFragment : Fragment(R.layout.register_level_fragment){

    private var binding: RegisterLevelFragmentBinding? = null

    private val viewModel: RegisterViewModel by hiltNavGraphViewModels(R.id.register_graph)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = RegisterLevelFragmentBinding.bind(view)

        viewModel.viewState.observe(viewLifecycleOwner) {
            renderUi(it)
        }

        viewModel.registerEvent.observe(viewLifecycleOwner) {
            if (it.getData() is RegisterEvent.RegistrationSucceeded) {
                findNavController().navigate(
                    RegisterLevelFragmentDirections.actionRegisterLevelFragmentToProjectListFragment()
                )
            }
        }

        binding?.apply {
            btnBeginner.setOnClickListener {
                viewModel.onLevel(Level.BEGINNER)
            }

            btnIntermediate.setOnClickListener {
                viewModel.onLevel(Level.INTERMEDIATE)
            }

            btnAdvance.setOnClickListener {
                viewModel.onLevel(Level.ADVANCED)
            }
        }
    }

    private fun renderUi(viewState: RegisterViewState) = viewState.apply {
        binding?.apply {
            scrim.root.isVisible = loading
            includeBtnNext.btnNext.isEnabled = !loading

            btnBeginner.isEnabled = !loading
            btnIntermediate.isEnabled = !loading
            btnAdvance.isEnabled = !loading
        }
    }

}