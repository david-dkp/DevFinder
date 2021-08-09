package fr.feepin.devfinder.ui.profile

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import fr.feepin.devfinder.R
import fr.feepin.devfinder.data.models.Level
import fr.feepin.devfinder.databinding.ProfileFragmentBinding
import fr.feepin.devfinder.ui.projectlist.ProjectListAdapter

@AndroidEntryPoint
class ProfileFragment : Fragment(R.layout.profile_fragment) {

    private val args: ProfileFragmentArgs by navArgs()

    private var binding: ProfileFragmentBinding? = null

    private val viewModel: ProfileViewModel by viewModels()

    private var technologiesAdapter: ProfileTechnologyAdapter? = null
    private var projectListAdapter: ProjectListAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = ProfileFragmentBinding.bind(view)

        binding?.toolbar?.setupWithNavController(findNavController())

        setupClickListeners()
        setupRecyclerViews()
        setupStatesListeners()
    }

    override fun onStart() {
        super.onStart()
        viewModel.fetchUserAndProjects(args.userId)
    }

    private fun setupClickListeners() {
        binding?.ivChat?.setOnClickListener {
            viewModel.onChatButtonClick()
        }
    }

    private fun setupRecyclerViews() {
        technologiesAdapter = ProfileTechnologyAdapter()

        binding?.rvTechnologies?.apply {
            adapter = technologiesAdapter
            layoutManager =
                StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.HORIZONTAL)
        }

        projectListAdapter = ProjectListAdapter {
            findNavController().navigate(
                ProfileFragmentDirections.actionProfileFragmentToProjectFragment(it.userId, it.id!!)
            )
        }

        binding?.rvProjects?.apply {
            adapter = projectListAdapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }

    }

    private fun setupStatesListeners() {
        viewModel.viewState.observe(viewLifecycleOwner) {
            renderUi(it)
        }

        viewModel.event.observe(viewLifecycleOwner) {
            it.getData()?.let {
                renderEvent(it)
            }
        }
    }

    private fun renderUi(viewState: ProfileViewState) {
        renderUser(viewState.userState)
        renderProjects(viewState.projectsState)
    }

    private fun renderEvent(profileEvent: ProfileEvent) {
        if (profileEvent is ProfileEvent.EnterChat) {
            findNavController().navigate(
                ProfileFragmentDirections.actionToChatFragment(profileEvent.chatId)
            )
        }
    }

    private fun renderUser(userState: ProfileViewState.UserState) {
        binding?.apply {
            userState?.apply {
                if (!userState.loading) {
                    user.apply {
                        Glide.with(requireContext())
                            .load(userState.user.profilePictureUrl)
                            .into(ivProfilePicture)

                        tvName.text = username
                        tvLevel.text = Level.getLevelByNumber(level)?.let {
                            getString(it.textResId)
                        } ?: ""
                        tvDescription.text = bio
                        technologiesAdapter?.submitList(technologies)
                    }
                    ivChat.isVisible = !isSelfUser
                }
            }
        }
    }

    private fun renderProjects(projectsState: ProfileViewState.ProjectsState) {
        projectListAdapter?.submitList(projectsState.projects)
    }

}