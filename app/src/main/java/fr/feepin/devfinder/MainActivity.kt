package fr.feepin.devfinder

import android.os.Bundle
import android.transition.TransitionManager
import android.util.Log
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import androidx.activity.viewModels
import androidx.annotation.MenuRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import fr.feepin.devfinder.data.auth.AuthState
import fr.feepin.devfinder.data.models.User
import fr.feepin.devfinder.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), NavController.OnDestinationChangedListener{

    private var binding: ActivityMainBinding? = null

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setupToolbar()
        setupStateListeners()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding?.toolbar)
        binding?.ivProfilePicture?.setOnClickListener {
            showMenu(it, R.menu.activity_main_profile_menu)
        }
    }

    private fun showMenu(v: View, @MenuRes menuRes: Int) {
        val popup = PopupMenu(this, v)
        popup.menuInflater.inflate(menuRes, popup.menu)

        popup.setOnMenuItemClickListener { menuItem: MenuItem ->
            viewModel.onMenuItemClick(menuItem)
        }

        popup.show()
    }

    private fun setupStateListeners() {
        lifecycleScope.launchWhenStarted {
            viewModel.authState.collect {
                if (it is AuthState.Unauthenticated) {
                    findNavController(R.id.fragmentContainerView).navigate(MainNavGraphDirections.actionToLoginNavigation())
                }
            }
        }

        lifecycleScope.launch(Dispatchers.Main) {
            viewModel.userState.collect {
                it?.let {
                    updateUserUi(it)
                }
            }
        }

        viewModel.event.observe(this) {
            it.getData()?.let {
                renderEvent(it)
            }
        }
    }

    private fun updateUserUi(user: User) {
        binding?.apply {
            Glide.with(this@MainActivity)
                .load(user.profilePictureUrl)
                .into(ivProfilePicture)
        }
    }

    private fun renderEvent(event: MainEvent) {
        if (event is MainEvent.GoToProfile) {
            findNavController(R.id.fragmentContainerView).navigate(
                MainNavGraphDirections.actionToProfileFragment(event.userId)
            )
        }
    }

    override fun onStart() {
        super.onStart()
        setupBars()
        viewModel.userGoesOnline()
    }

    private fun setupBars() {
        val navController = findNavController(R.id.fragmentContainerView)
        navController.addOnDestinationChangedListener(this)
        binding?.toolbar?.setupWithNavController(navController)
        binding?.botNav?.setupWithNavController(navController)
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
        R.id.projectFragment,
        R.id.addProjectFragment
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