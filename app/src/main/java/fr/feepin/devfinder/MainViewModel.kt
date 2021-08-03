package fr.feepin.devfinder

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Timestamp
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.feepin.devfinder.data.auth.AuthManager
import fr.feepin.devfinder.data.models.Status
import fr.feepin.devfinder.data.repos.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    authManager: AuthManager,
    private val userRepository: UserRepository
) : ViewModel() {

    val authState = authManager.authState

    val userState = userRepository.getUser()

    fun userGoesOnline() {
        authState.value.uid?.let {
            viewModelScope.launch(Dispatchers.IO) {
                userRepository.setUserStatus(
                    it,
                    Status(it, true, Timestamp.now())
                )
            }
        }
    }

    fun userGoesOffline() {
        authState.value.uid?.let {
            viewModelScope.launch(Dispatchers.IO) {
                userRepository.setUserStatus(
                    it,
                    Status(it, false, Timestamp.now())
                )
            }
        }
    }
}