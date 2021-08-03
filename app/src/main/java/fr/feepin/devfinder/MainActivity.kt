package fr.feepin.devfinder

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import fr.feepin.devfinder.data.auth.AuthState
import fr.feepin.devfinder.databinding.ActivityMainBinding
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), NavController.OnDestinationChangedListener{

    private var binding: ActivityMainBinding? = null

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        lifecycleScope.launchWhenStarted {
            viewModel.authState.collect {
                if (it is AuthState.Unauthenticated) {
                    findNavController(R.id.fragmentContainerView).setGraph(R.navigation.login_graph)
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        val navController = findNavController(R.id.fragmentContainerView)
        navController.addOnDestinationChangedListener(this)
        binding?.toolbar?.setupWithNavController(navController)
        binding?.botNav?.setupWithNavController(navController)
        viewModel.userGoesOnline()
    }

    override fun onStop() {
        super.onStop()
        viewModel.userGoesOffline()
    }

    private val noBotNavFragments = listOf(
        R.id.profileFragment,
        R.id.loginFragment,
        R.id.registerDescribeFragment,
        R.id.registerLevelFragment,
        R.id.registerTechnologiesFragment,
        R.id.chatFragment,
        R.id.projectFragment
    )

    override fun onDestinationChanged(
        controller: NavController,
        destination: NavDestination,
        arguments: Bundle?
    ) {
        if (destination.id in noBotNavFragments) {
            hideBars()
        } else {
            showBars()
        }
    }

    private fun hideBars() {
        binding?.botNav?.visibility = View.GONE
        binding?.toolbar?.visibility = View.GONE
    }

    private fun showBars() {
        binding?.botNav?.visibility = View.VISIBLE
        binding?.toolbar?.visibility = View.VISIBLE
    }

}