package fr.feepin.devfinder.ui.project

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import fr.feepin.devfinder.R
import fr.feepin.devfinder.databinding.ProjectFragmentBinding

class ProjectFragment : Fragment(R.layout.project_fragment){

    private var binding: ProjectFragmentBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = ProjectFragmentBinding.bind(view)

    }

}