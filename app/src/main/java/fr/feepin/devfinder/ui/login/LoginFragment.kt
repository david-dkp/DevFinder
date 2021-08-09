package fr.feepin.devfinder.ui.login

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.OAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import fr.feepin.devfinder.R
import fr.feepin.devfinder.activitycontracts.GoogleSignInActivityContract
import fr.feepin.devfinder.databinding.LoginFragmentBinding

@AndroidEntryPoint
class LoginFragment : Fragment(R.layout.login_fragment) {

    private var binding: LoginFragmentBinding? = null

    private val googleSignInLauncher = getGoogleSignInLauncher()

    private val viewModel: LoginViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = LoginFragmentBinding.bind(view)

        setupListeners()

        viewModel.loginState.observe(viewLifecycleOwner) {
            renderUi(it)
        }
    }

    private fun renderUi(loginState: LoginState) {
        when (loginState) {
            is LoginState.Success -> {
                toggleLoading(false)
                if (loginState.newUser) {
                    navigateToRegister()
                } else {
                    navigateToMainFragment()
                }
            }
            is LoginState.Loading -> {
                toggleLoading(true)

            }
            is LoginState.Error -> {
                toggleLoading(false)
                Toast.makeText(requireContext(), loginState.message, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun toggleLoading(loading: Boolean) {
        binding?.scrim?.root?.isVisible = loading
        binding?.btnAboutDevFinder?.isEnabled = !loading
    }

    private fun navigateToRegister() {
        val actionToRegisterGraph = LoginFragmentDirections.actionLoginFragmentToRegisterGraph()
        findNavController().navigate(actionToRegisterGraph)
    }

    private fun navigateToMainFragment() {
        val actionToProjectListFragment =
            LoginFragmentDirections.actionLoginFragmentToProjectListFragment()
        findNavController().navigate(actionToProjectListFragment)
    }

    private fun getGoogleSignInLauncher(): ActivityResultLauncher<GoogleSignInOptions> {
        return registerForActivityResult(
            GoogleSignInActivityContract()
        ) {
            viewModel.onAuthCredential(it)
        }
    }

    private fun setupListeners() {
        binding?.imbtnGoogle?.setOnClickListener {
            viewModel.onStartLogin()
            signInWithGoogle()
        }

        binding?.imbtnGithub?.setOnClickListener {
            viewModel.onStartLogin()
            signInWithGithub()
        }

        binding?.imbtnTwitter?.setOnClickListener {
            viewModel.onStartLogin()
            signInWithTwitter()
        }

        binding?.btnAboutDevFinder?.setOnClickListener {
            showAboutDevFinderDialog()
        }
    }

    private fun showAboutDevFinderDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.app_name)
            .setMessage(R.string.devfinder_description_text)
            .show()
    }

    private fun signInWithGoogle() {
        val gso = GoogleSignInOptions.Builder()
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestProfile()
            .build()

        googleSignInLauncher.launch(gso)
    }

    private fun signInWithGithub() {
        val provider = OAuthProvider.newBuilder("github.com").build()
        startActivityForProvider(provider)
    }

    private fun signInWithTwitter() {
        val provider = OAuthProvider.newBuilder("twitter.com").build()
        startActivityForProvider(provider)
    }

    private fun startActivityForProvider(provider: OAuthProvider) {
        Firebase.auth.startActivityForSignInWithProvider(requireActivity(), provider)
            .addOnSuccessListener {
                viewModel.onAuthCredential(it.credential)
            }
            .addOnFailureListener {
                viewModel.onLoginError(it)
            }
    }

}