package fr.feepin.devfinder.data

import fr.feepin.devfinder.data.models.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun listenUserById(id: String): Flow<User>
    suspend fun fetchUserById(id: String): User
    suspend fun addUser(user: User)
}