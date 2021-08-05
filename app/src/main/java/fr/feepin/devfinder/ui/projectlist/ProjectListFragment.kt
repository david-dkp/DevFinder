package fr.feepin.devfinder.ui.projectlist

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import fr.feepin.devfinder.R
import fr.feepin.devfinder.databinding.ProjectListFragmentBinding

@AndroidEntryPoint
class ProjectListFragment : Fragment(R.layout.project_list_fragment) {

    private var binding: ProjectListFragmentBinding? = null

    private var projectListAdapter: ProjectListAdapter? = null

    private val viewModel: ProjectListViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = ProjectListFragmentBinding.bind(view)

        setupAdapter()
        setupStatesListeners()

        binding?.fabPostProject?.setOnClickListener {
            findNavController().navigate(
                ProjectListFragmentDirections.actionProjectListFragmentToAddProjectFragment(),
                FragmentNavigatorExtras(
                    binding!!.fabPostProject to "addproject_root"
                )
            )
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.fetchProjects()
    }

    private fun setupAdapter() {
        projectListAdapter = ProjectListAdapter {
            val direction = ProjectListFragmentDirections.actionGlobalProjectFragment(it.userId, it.id!!)
            findNavController().navigate(direction)
        }

        binding?.rvProjects?.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = projectListAdapter
        }
    }

    private fun setupStatesListeners() {
        viewModel.viewState.observe(viewLifecycleOwner) {
            renderUi(it)
        }
    }

    private fun renderUi(viewState: ProjectListViewState) {
        binding?.progressBar?.isVisible = viewState.isLoading
        projectListAdapter?.submitList(viewState.projects)
    }

}