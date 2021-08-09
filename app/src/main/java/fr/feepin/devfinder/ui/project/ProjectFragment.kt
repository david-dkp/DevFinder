package fr.feepin.devfinder.ui.project

import android.os.Bundle
import android.view.ContextMenu
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import fr.feepin.devfinder.R
import fr.feepin.devfinder.databinding.ProjectFragmentBinding
import fr.feepin.devfinder.ui.profile.ProfileTechnologyAdapter

@AndroidEntryPoint
class ProjectFragment : Fragment(R.layout.project_fragment) {

    private val args: ProjectFragmentArgs by navArgs()

    private var technologiesAdapter: ProfileTechnologyAdapter? = null

    private val viewModel: ProjectViewModel by viewModels()

    private var binding: ProjectFragmentBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = ProjectFragmentBinding.bind(view)

        binding?.toolbar?.setupWithNavController(findNavController())

        setupTechnologiesList()
        setupStatesListeners()
        setupClickListeners()

        viewModel.fetchProject(args.userId, args.projectId)
    }

    private fun setupTechnologiesList() {
        technologiesAdapter = ProfileTechnologyAdapter()
        binding?.rvTechnologies?.apply {
            adapter = technologiesAdapter
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

    private fun setupClickListeners() {
        val onProfileClickListener: (View) -> Unit = {
            val userId = viewModel.viewState.value?.project?.userId

            userId?.let {
                findNavController().navigate(
                    ProjectFragmentDirections.actionProjectFragmentToProfileFragment(it)
                )
            }
        }

        binding?.apply {
            ivProfilePicture.setOnClickListener(onProfileClickListener)
            tvUsername.setOnClickListener(onProfileClickListener)
        }

        binding?.btnStartConversation?.setOnClickListener {
            viewModel.onStartConversionClick()
        }
    }

    private fun renderUi(viewState: ProjectViewState) {
        viewState.project?.let { project ->
            binding?.apply {
                tvTitle.text = project.title
                tvUsername.text = project.username
                tvDescription.text = project.description
                tvInterestedProject.isVisible = !viewState.userProject
                btnStartConversation.isVisible = !viewState.userProject
                technologiesAdapter?.submitList(project.technologies)
                Glide.with(requireContext())
                    .load(project.profilePictureUrl)
                    .into(ivProfilePicture)
            }
        }
    }

    private fun renderEvent(event: ProjectEvent) {
        if (event is ProjectEvent.EnterChat) {
            findNavController().navigate(
                ProjectFragmentDirections.actionToChatFragment(event.chatId)
            )
        }
    }

}