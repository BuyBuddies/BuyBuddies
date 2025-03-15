package com.pwojtowicz.buybuddies.data.repository

import android.util.Log
import com.pwojtowicz.buybuddies.auth.AuthorizationClient
import com.pwojtowicz.buybuddies.data.api.AuthApiService
import com.pwojtowicz.buybuddies.data.dao.GroceryListDao
import com.pwojtowicz.buybuddies.data.dao.UserDao
import com.pwojtowicz.buybuddies.data.entity.User
import retrofit2.HttpException
import java.time.LocalDateTime
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userDao: UserDao,
    private val authClient: AuthorizationClient,
    private val authApiService: AuthApiService,
) {

    suspend fun fetchUserData() {
        Log.i(TAG, "Fetching user data")
        try {
            val currentFirebaseUser = authClient.getSignedInUser()
                ?: throw IllegalStateException("No user signed in")

            val remoteUser = authApiService.getUserData()

            val user = User(
                firebaseUid = currentFirebaseUser.firebaseUid,
                name = remoteUser.name,
                email = remoteUser.email,
                updatedAt = remoteUser.updatedAt ?: System.currentTimeMillis(),
                createdAt = remoteUser.createdAt ?: LocalDateTime.now().toString()
            )

            userDao.syncUser(user)
            Log.i(TAG, "Successfully synced user data to local DB: ${user.firebaseUid}")
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching user data", e)
            throw handleApiError(e)
        }
    }

    suspend fun insertUser(user: User): Long {
        try {
            return userDao.insert(user)
        } catch (e: Exception) {
            Log.e("UserRepository", "Error inserting user", e)
            throw e
        }
    }

    suspend fun deleteAll() {
        try {
            userDao.deleteAll()
        } catch (e: Exception) {
            Log.e("UserRepository", "Error deleting all users", e)
            throw e
        }
    }

    suspend fun saveUser(user: User) {
        userDao.insert(user)
    }

    private fun handleApiError(e: Exception): Throwable {
        return when (e) {
            is HttpException -> when (e.code()) {
                401 -> IllegalStateException("Authentication failed - please log in again")
                403 -> IllegalStateException("Not authorized for this operation")
                404 -> IllegalStateException("List not found")
                else -> IllegalStateException("Server error: ${e.message}")
            }
            else -> e
        }
    }

    companion object {
        private const val TAG = "UserRepository"
    }
}