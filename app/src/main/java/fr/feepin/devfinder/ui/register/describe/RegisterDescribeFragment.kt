package fr.feepin.devfinder.ui.register.describe

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import fr.feepin.devfinder.R
import fr.feepin.devfinder.databinding.RegisterDescribeFragmentBinding
import fr.feepin.devfinder.ui.register.RegisterEvent
import fr.feepin.devfinder.ui.register.RegisterViewModel
import fr.feepin.devfinder.ui.register.RegisterViewState

@AndroidEntryPoint
class RegisterDescribeFragment : Fragment(R.layout.register_describe_fragment) {

    private var binding: RegisterDescribeFragmentBinding? = null

    private val viewModel: RegisterViewModel by hiltNavGraphViewModels(R.id.register_graph)

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            showCancelRegistrationDialog()
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = RegisterDescribeFragmentBinding.bind(view)

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, onBackPressedCallback)

        viewModel.viewState.observe(viewLifecycleOwner) {
            renderUi(it)
        }

        viewModel.registerEvent.observe(viewLifecycleOwner) {
            it.getData()?.let {
                handleEvent(it)
            }
        }

        binding?.apply {
            includeBtnNext.btnNext.setOnClickListener {
                viewModel.onUsername(inputNickname.editText!!.text.toString())
                viewModel.onBio(inputBio.editText!!.text.toString())
                findNavController().navigate(
                    RegisterDescribeFragmentDirections.actionRegisterDescribeFragmentToRegisterTechnologiesFragment()
                )
            }
        }
    }

    private fun handleEvent(event: RegisterEvent) {
        if (event is RegisterEvent.RegistrationCancelled) {
            findNavController().navigateUp()
        }
    }

    private fun showCancelRegistrationDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.cancel_registration_title)
            .setMessage(R.string.do_you_cancel_registration)
            .setPositiveButton(
                R.string.yes_text
            ) { dialog, which ->
                viewModel.onCancelRegistration()
            }
            .setNegativeButton(R.string.no_text) { dialog, which ->
                dialog.cancel()
            }
            .setCancelable(true)
            .show()
    }

    private fun renderUi(viewState: RegisterViewState) = viewState.apply {
        binding?.apply {
            scrim.root.isVisible = loading
            includeBtnNext.btnNext.isEnabled = !loading
        }
    }

}