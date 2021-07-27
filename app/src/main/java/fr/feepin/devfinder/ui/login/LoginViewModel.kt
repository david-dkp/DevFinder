package fr.feepin.devfinder.ui.login

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.AuthCredential
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import fr.feepin.devfinder.R
import fr.feepin.devfinder.data.auth.AuthManager
import fr.feepin.devfinder.data.auth.AuthResult
import fr.feepin.devfinder.data.repos.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class LoginViewModel
@Inject constructor(
    @ApplicationContext val context: Context,
    val authManager: AuthManager,
    val userRepository: UserRepository
) : ViewModel() {

    private val _loginState = MutableLiveData<LoginState>()
    val loginState: LiveData<LoginState> = _loginState


    fun onStartLogin() {
        _loginState.value = LoginState.Loading
    }

    fun onAuthCredential(authCredential: AuthCredential?) {
        authCredential?.let {
            viewModelScope.launch(Dispatchers.IO) {
                val result = authManager.signInWithCredential(authCredential)

                withContext(Dispatchers.Main) {
                    updateViewState(result)
                }
            }
        } ?: kotlin.run {
            _loginState.value = LoginState.Error(context.getString(R.string.auth_error_message))
        }
    }

    private fun updateViewState(result: AuthResult) {
        _loginState.value = when (result) {
            is AuthResult.Success -> LoginState.Success
            is AuthResult.Failed ->
                LoginState.Error(
                    result.exception.localizedMessage
                        ?: context.getString(R.string.auth_error_message)
                )
        }
    }

}