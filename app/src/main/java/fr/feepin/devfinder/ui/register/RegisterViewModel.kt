package fr.feepin.devfinder.ui.register

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Timestamp
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import fr.feepin.devfinder.data.auth.AuthManager
import fr.feepin.devfinder.data.auth.AuthState
import fr.feepin.devfinder.data.models.Level
import fr.feepin.devfinder.data.models.User
import fr.feepin.devfinder.data.repos.UserRepository
import fr.feepin.devfinder.utils.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

@HiltViewModel
class RegisterViewModel(
    @ApplicationContext val context: Context,
    val authManager: AuthManager,
    val userRepository: UserRepository
) : ViewModel() {

    private val usernameLiveData = MutableLiveData<String>()
    private val bioLiveData = MutableLiveData("")
    private val technologiesLiveData = MutableLiveData<List<String>>(Collections.emptyList())
    private val levelLiveData = MutableLiveData<Level>()

    private val viewState = MutableLiveData<RegisterViewState>()

    private val _registerEvent = MutableLiveData<Event<Boolean>>()
    val registerEvent: LiveData<Event<Boolean>> = _registerEvent

    fun onUsername(username: String) {
        usernameLiveData.value
    }

    fun onBio(bio: String) {
        bioLiveData.value
    }

    fun onTechnologies(technologies: List<String>) {
        technologiesLiveData.value = technologies
    }

    fun onLevel(level: Level) {
        levelLiveData.value = level

        viewState.value = RegisterViewState(true)

        viewModelScope.launch(Dispatchers.IO) {

            if (authManager.authState.value is AuthState.Authenticated) {
                val user = User(
                    null,
                    levelLiveData.value!!.level,
                    authManager.authState.value.profileImageUrl!!,
                    technologiesLiveData.value!!,
                    usernameLiveData.value!!,
                    bioLiveData.value!!,
                    Timestamp.now(),
                    Collections.emptyList()
                )

                userRepository.addUser(user)

                _registerEvent.value = Event(true)
            } else {
                _registerEvent.value = Event(false)
            }

        }

    }

}