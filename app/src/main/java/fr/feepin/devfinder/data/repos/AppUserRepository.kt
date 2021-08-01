package fr.feepin.devfinder.data.repos

import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import fr.feepin.devfinder.data.models.Project
import fr.feepin.devfinder.data.models.User
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
private const val TAG = "AppUserRepository"

interface AppUserRepository {

    //Here I wanna listen in real time the changes in the User document with the id of :id
    //You can by using Document.addSnapshotListener(...) and converting that callback base API to a Flow
    //To convert into flow: https://medium.com/swlh/callback-flows-in-android-d2c6ed5bc488

    private val userCollectionRef get() = Firebase.firestore.collection("users")
    private val projectCollectionRef get() = Firebase.firestore.collection("projects")

    fun listenUserById(id: String): Flow<User>{
        val flow = callbackFlow<User>{
            val userToListenToRef = userCollectionRef.document(id)
            val registration = userToListenToRef.addSnapshotListener {value, error ->
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
        return flow
    }

    //Just get the User from the "users" collection by id
    suspend fun fetchUserById(id: String): User{
        val fetchUser = userCollectionRef.document(id).get().await()
        val user = fetchUser.toObject<User>()
        return user!!
    }

    //Here I want to add the document User in the "users" collection.
    suspend fun addUser(user: User){
        userCollectionRef.add(user).await()
    }
}