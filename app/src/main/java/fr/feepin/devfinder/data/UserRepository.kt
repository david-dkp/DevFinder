package fr.feepin.devfinder.data

import fr.feepin.devfinder.data.models.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    //Here I wanna listen in real time the changes in the User document with the id of :id
    //You can by using Document.addSnapshotListener(...) and converting that callback base API to a Flow
    //To convert into flow: https://medium.com/swlh/callback-flows-in-android-d2c6ed5bc488
    fun listenUserById(id: String): Flow<User>

    //Just get the User from the "users" collection by id
    suspend fun fetchUserById(id: String): User

    //Here I want to add the document User in the "users" collection.
    suspend fun addUser(user: User)
}