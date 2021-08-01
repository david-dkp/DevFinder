package fr.feepin.devfinder.ui.addproject

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import fr.feepin.devfinder.R
import fr.feepin.devfinder.databinding.AddProjectFragmentBinding

class AddProjectFragment : Fragment(R.layout.add_project_fragment) {

    private var binding: AddProjectFragmentBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = AddProjectFragmentBinding.bind(view)

    }

}