package fr.feepin.devfinder.ui.projectlist

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import fr.feepin.devfinder.R
import fr.feepin.devfinder.databinding.ProjectListFragmentBinding

class ProjectListFragment : Fragment(R.layout.project_list_fragment) {

    private var binding: ProjectListFragmentBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = ProjectListFragmentBinding.bind(view)
    }

}