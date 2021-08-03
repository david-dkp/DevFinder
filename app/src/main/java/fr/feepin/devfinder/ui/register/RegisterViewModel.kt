package fr.feepin.devfinder.ui.register

import android.content.Context
import android.util.Log
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
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception
import java.util.*
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val authManager: AuthManager,
    private val userRepository: UserRepository
) : ViewModel() {

    private val usernameLiveData = MutableLiveData<String>()
    private val bioLiveData = MutableLiveData("")
    private val technologiesLiveData = MutableLiveData<List<String>>(Collections.emptyList())
    private val levelLiveData = MutableLiveData<Level>()

    private val _viewState = MutableLiveData<RegisterViewState>()
    val viewState: LiveData<RegisterViewState> = _viewState

    private val _registerEvent = MutableLiveData<Event<RegisterEvent>>()
    val registerEvent: LiveData<Event<RegisterEvent>> = _registerEvent

    fun onUsername(username: String) {
        usernameLiveData.value = username
    }

    fun onBio(bio: String) {
        bioLiveData.value = bio
    }

    fun onTechnologies(technologies: List<String>) {
        technologiesLiveData.value = technologies
    }

    fun onLevel(level: Level) {
        levelLiveData.value = level

        saveUserToDatabase()
    }

    private fun saveUserToDatabase() {
        _viewState.value = RegisterViewState(true)

        viewModelScope.launch(Dispatchers.IO) {

            if (authManager.authState.value is AuthState.Authenticated) {
                try {
                    val user = User(
                        null,
                        levelLiveData.value!!.level,
                        authManager.authState.value.profileImageUrl ?: "",
                        technologiesLiveData.value!!,
                        usernameLiveData.value!!,
                        bioLiveData.value!!,
                        Timestamp.now(),
                        Collections.emptyList()
                    )
                    userRepository.addUser(user)

                    withContext(Dispatchers.Main) {
                        _viewState.value = RegisterViewState(false)
                        _registerEvent.value = Event(RegisterEvent.RegistrationSucceeded)
                    }
                } catch (e: Exception) {
                    Log.d("debug", e.message ?: "sdfsd")
                }


            } else {
                withContext(Dispatchers.Main) {
                    _viewState.value = RegisterViewState(false)
                    _registerEvent.value = Event(RegisterEvent.RegistrationFailed)
                }
            }
        }
    }

    fun onCancelRegistration() {
        _viewState.value = RegisterViewState(true)

        viewModelScope.launch(Dispatchers.IO) {
            authManager.deleteUser()

            withContext(Dispatchers.Main) {
                _viewState.value = RegisterViewState(false)
                _registerEvent.value = Event(RegisterEvent.RegistrationCancelled)
            }
        }
    }

}