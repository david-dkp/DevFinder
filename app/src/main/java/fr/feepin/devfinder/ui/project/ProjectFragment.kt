package fr.feepin.devfinder.ui.project

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import fr.feepin.devfinder.R
import fr.feepin.devfinder.databinding.ProjectFragmentBinding

class ProjectFragment : Fragment(R.layout.project_fragment){

    private val viewModel: ProjectViewModel by viewModels()

    private var binding: ProjectFragmentBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = ProjectFragmentBinding.bind(view)

        viewModel.viewState.observe(viewLifecycleOwner) {
            renderUi(it)
        }

        binding?.toolbar?.setupWithNavController(findNavController())

        val onProfileClickListener: (View) -> Unit = {
            val userId = viewModel.viewState.value?.project?.userId

            userId?.let {
                findNavController().navigate(
                    ProjectFragmentDirections.actionToProfileFragment(it)
                )
            }
        }

        binding?.apply {
            ivProfilePicture.setOnClickListener(onProfileClickListener)
            tvUsername.setOnClickListener(onProfileClickListener)
        }
    }

    private fun renderUi(viewState: ProjectViewState) {
        viewState.project?.let { project ->
            binding?.apply {
                tvTitle.text = project.title
                tvUsername.text = project.username
                Glide.with(requireContext())
                    .load(project.profilePictureUrl)
                    .into(ivProfilePicture)
            }
        }
    }

}