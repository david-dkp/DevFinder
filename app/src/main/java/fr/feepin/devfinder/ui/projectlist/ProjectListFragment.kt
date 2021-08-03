package fr.feepin.devfinder.ui.projectlist

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import fr.feepin.devfinder.R
import fr.feepin.devfinder.databinding.ProjectListFragmentBinding

@AndroidEntryPoint
class ProjectListFragment : Fragment(R.layout.project_list_fragment) {

    private var binding: ProjectListFragmentBinding? = null

    private val viewModel: ProjectListViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = ProjectListFragmentBinding.bind(view)

        val adapter = ProjectListAdapter {
            val direction = ProjectListFragmentDirections.actionGlobalProjectFragment(it.id!!)
        }

        viewModel.viewState.observe(viewLifecycleOwner) {
            render(it)
        }

        binding?.fabPostProject?.setOnClickListener {
            findNavController().navigate(
                ProjectListFragmentDirections.actionProjectListFragmentToAddProjectFragment(),
                FragmentNavigatorExtras(
                    binding!!.fabPostProject to "addproject_root"
                )
            )
        }
    }

    private fun render(viewState: ProjectListViewState) {

    }

}