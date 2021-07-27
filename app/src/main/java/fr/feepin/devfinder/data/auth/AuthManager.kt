package fr.feepin.devfinder.data.auth

import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.sendBlocking
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@ExperimentalCoroutinesApi
class AuthManager @Inject constructor() {

    @ExperimentalCoroutinesApi
    private val currentUserFlow = callbackFlow {

        val listener: (FirebaseAuth) -> Unit = {
            sendBlocking(it.currentUser)
        }

        Firebase.auth.addAuthStateListener(listener)

        awaitClose {
            Firebase.auth.removeAuthStateListener(listener)
        }
    }

    private val _authState: MutableStateFlow<AuthState> =
        MutableStateFlow(AuthState.Unauthenticated)
    val authState: StateFlow<AuthState> = _authState

    init {
        GlobalScope.launch {
            currentUserFlow.collect { firebaseUser ->
                _authState.value = firebaseUser?.let {
                    AuthState.Authenticated(it.uid)
                } ?: AuthState.Unauthenticated
            }
        }
    }

    /**
     * Used to sign in with firebase with an [authCredential]
     * @return an [AuthResult] that state whether the login process has succeeded or not.
     */
    suspend fun signInWithCredential(authCredential: AuthCredential): AuthResult {
        _authState.value = AuthState.Authenticating

        return try {
            val firebaseAuthResult = Firebase.auth.signInWithCredential(authCredential).await()
            val authResult = AuthResult.Success(
                firebaseAuthResult.user!!.uid,
                firebaseAuthResult?.additionalUserInfo?.isNewUser ?: true
            )
            _authState.value = AuthState.Authenticated(authResult.uid)

            authResult
        } catch (e: Exception) {
            _authState.value = AuthState.Unauthenticated
            AuthResult.Failed(e)
        }

    }

    fun signOut() {
        Firebase.auth.signOut()
    }

}