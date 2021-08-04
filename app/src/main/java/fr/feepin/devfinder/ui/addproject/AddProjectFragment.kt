package fr.feepin.devfinder.ui.addproject

import android.graphics.Color
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.setupWithNavController
import androidx.transition.ChangeBounds
import com.google.android.material.transition.MaterialContainerTransform
import dagger.hilt.android.AndroidEntryPoint
import fr.feepin.devfinder.R
import fr.feepin.devfinder.databinding.AddProjectFragmentBinding
import fr.feepin.devfinder.utils.themeColor

@AndroidEntryPoint
class AddProjectFragment : Fragment(R.layout.add_project_fragment) {

    private var binding: AddProjectFragmentBinding? = null

    private val viewModel: AddProjectViewModel by viewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = AddProjectFragmentBinding.bind(view)

        setupToolbar()
        setupStatesListeners()
        setupSharedElementTransitions()

    }

    private fun setupToolbar() {
        binding?.toolbar?.setupWithNavController(findNavController())
        binding?.toolbar?.setOnMenuItemClickListener {
            if (it.itemId == R.id.confirm) {
                binding?.apply {
                    viewModel.onConfirmClick(
                        inputTitle.editText!!.text.toString(),
                        inputDescription.editText!!.text.toString(),
                        inputTechnologies.editText!!.text.toString()
                    )
                }
                true
            }

            false
        }
    }

    private fun setupStatesListeners() {
        viewModel.viewState.observe(viewLifecycleOwner) {
            renderUi(it)
        }

        viewModel.event.observe(viewLifecycleOwner) { event ->
            event.getData()?.let {
                renderEvent(it)
            }
        }
    }

    private fun setupSharedElementTransitions() {
        sharedElementEnterTransition = MaterialContainerTransform().apply {
            startViewId = R.id.fabPostProject
            endView = binding!!.root

            scrimColor = Color.TRANSPARENT
            containerColor = requireContext().themeColor(R.attr.colorSurface)
            startContainerColor = requireContext().themeColor(R.attr.colorPrimary)
            endContainerColor = requireContext().themeColor(R.attr.colorSurface)
        }

        sharedElementReturnTransition = MaterialContainerTransform().apply {
            startView = binding!!.root
            endViewId = R.id.fabPostProject

            scrimColor = Color.TRANSPARENT
            containerColor = requireContext().themeColor(R.attr.colorSurface)
            startContainerColor = requireContext().themeColor(R.attr.colorSurface)
            endContainerColor = requireContext().themeColor(R.attr.colorPrimary)
        }
    }

    private fun renderEvent(event: AddProjectEvent) {
        if (event is AddProjectEvent.ProjectAdded) {
            findNavController().navigate(
                AddProjectFragmentDirections.actionAddProjectFragmentToProjectListFragment()
            )
        }
    }

    private fun renderUi(viewState: AddProjectViewState) {
        binding?.scrim?.root?.isVisible = viewState.loading

        if (viewState.titleError.isNotEmpty()) {
            binding?.inputTitle?.editText!!.setText(viewState.titleError)
        }

    }

}