package fr.feepin.devfinder

import android.view.MenuItem
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Timestamp
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.feepin.devfinder.data.auth.AuthManager
import fr.feepin.devfinder.data.models.Status
import fr.feepin.devfinder.data.repos.UserRepository
import fr.feepin.devfinder.utils.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val authManager: AuthManager,
    private val userRepository: UserRepository
) : ViewModel() {

    val authState = authManager.authState
    val userState = userRepository.getUser()

    private val _event = MutableLiveData<Event<MainEvent>>()
    val event: LiveData<Event<MainEvent>> = _event

    fun userGoesOnline() {
        userState.value?.id?.let {
            viewModelScope.launch(Dispatchers.IO) {
                userRepository.setUserStatus(
                    it,
                    Status(it, true, Timestamp.now())
                )
            }
        }
    }

    fun userGoesOffline() {
        userState.value?.id?.let {
            viewModelScope.launch(Dispatchers.IO) {
                userRepository.setUserStatus(
                    it,
                    Status(it, false, Timestamp.now())
                )
            }
        }
    }

    fun onMenuItemClick(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.itemMyProfile -> {
                authManager?.authState?.value?.uid?.let {
                    _event.value = Event(MainEvent.GoToProfile(it))
                }
                true
            }
            R.id.itemSignout -> {
                signOut()
                true
            }
            else -> false
        }
    }

    private fun signOut() {
        authManager.signOut()
    }
}