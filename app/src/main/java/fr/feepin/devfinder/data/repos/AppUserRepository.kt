package fr.feepin.devfinder.data.repos

import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import fr.feepin.devfinder.data.auth.AuthManager
import fr.feepin.devfinder.data.models.Status
import fr.feepin.devfinder.data.models.User
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

private const val TAG = "AppUserRepository"

@Singleton
class AppUserRepository @Inject constructor(val authManager: AuthManager) : UserRepository {

    private val userCollectionRef get() = Firebase.firestore.collection("users")

    private val userFlow = authManager.authState
        .flatMapLatest {

            if (it.uid == null) {
                return@flatMapLatest flowOf(null)
            }

            val flow = callbackFlow<User> {
                val userToListenToRef = userCollectionRef.document(it.uid)
                val registration = userToListenToRef.addSnapshotListener { value, error ->
                    //value is a DocumentSnapshot but we want a Project here which is a data class so we convert it
                    if (error != null) {
                        Log.w(TAG, "listen:error", error)
                        return@addSnapshotListener
                    }
                    val user = value?.toObject<User>()
                    trySendBlocking(user!!)
                }
                //This method will prevent the flow from ending, we wanna end until the scope where this flow gets collected dies
                awaitClose {
                    registration.remove()
                }
            }

            flow
        }.stateIn(GlobalScope, SharingStarted.Lazily, null)

    override fun getUser(): StateFlow<User?> {
        return userFlow
    }

    override suspend fun fetchUserById(id: String): User? {
        val fetchUser = userCollectionRef.document(id).get().await()
        val user = fetchUser.toObject<User>()
        return user
    }

    override suspend fun addUser(user: User) {
        userCollectionRef.add(user).await()
    }

    override suspend fun setUserStatus(userId: String, status: Status) {
        val statusCollectionRef = Firebase.firestore.collection("status")
        statusCollectionRef.document(userId).set(status).await()
    }

}