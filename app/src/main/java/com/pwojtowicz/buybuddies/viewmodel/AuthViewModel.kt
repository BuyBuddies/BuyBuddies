package com.pwojtowicz.buybuddies.viewmodel

import android.content.Intent
import android.content.IntentSender
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pwojtowicz.buybuddies.auth.AuthorizationClient
import com.pwojtowicz.buybuddies.auth.SignInResult
import com.pwojtowicz.buybuddies.auth.SignInState
import com.pwojtowicz.buybuddies.auth.UserData
import com.pwojtowicz.buybuddies.data.api.AuthApiService
import com.pwojtowicz.buybuddies.data.dto.UserDTO
import com.pwojtowicz.buybuddies.data.entity.User
import com.pwojtowicz.buybuddies.data.network.sync.DataSyncManager
import com.pwojtowicz.buybuddies.data.prefernces.PreferencesManager
import com.pwojtowicz.buybuddies.data.repository.GroceryListRepository
import com.pwojtowicz.buybuddies.data.repository.UserRepository
import com.pwojtowicz.buybuddies.ui.message.AppMessage
import com.pwojtowicz.buybuddies.ui.message.MessageHandler
import com.pwojtowicz.buybuddies.utility.InstallManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import retrofit2.HttpException
import java.io.IOException
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authClient: AuthorizationClient,
    private val preferencesManager: PreferencesManager,
    private val installManager: InstallManager,
    private val userRepository: UserRepository,
    private val groceryListRepository: GroceryListRepository,
    private val messageHandler: MessageHandler,
    private val authApiService: AuthApiService,
    private val dataSyncManager: DataSyncManager
) : ViewModel() {
    private val _state = MutableStateFlow(SignInState())
    val state = _state.asStateFlow()

    private val _currentUser = MutableStateFlow<UserData?>(null)
    val currentUser = _currentUser.asStateFlow()

    init {
        _currentUser.value = authClient.getSignedInUser()
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            checkIfSignedIn()
            _state.update { it.copy(isLoading = false) }
        }
    }

    fun getCurrentUser(): UserData? {
        val user = authClient.getSignedInUser()
        _currentUser.value = user
        return user
    }

    suspend fun signInWithIntent(intent: Intent): SignInResult {
        return authClient.signInWithIntent(intent)
    }

    suspend fun signIn(): IntentSender? {
        return authClient.signIn()
    }

    private fun checkIfSignedIn() {
        val currentUser = authClient.getSignedInUser()
        val isNewInstall = installManager.isNewInstall()

        Log.d(TAG, "Checking sign in - User: ${currentUser?.email}, IsNewInstall: $isNewInstall")

        if (isNewInstall && currentUser != null) {
            viewModelScope.launch {
                try {
                    _state.update { it.copy(
                        isSignedIn = false,
                        isSignInSuccessful = false
                    )}

                    authClient.signOut()

                    dataSyncManager.cancelSync()

                    Log.d(TAG, "Successfully signed out user on new install")
                } catch (e: Exception) {
                    Log.e(TAG, "Error signing out user on new install", e)
                }
            }
        } else {
            _state.update { it.copy(
                isSignedIn = currentUser != null
            )}

            if (currentUser != null) {
                viewModelScope.launch {
                    try {
                        dataSyncManager.setupPeriodicSync()
                    } catch (e: Exception) {
                        Log.e(TAG, "Error setting up sync", e)
                    }
                }
            }
        }
    }

    fun startSignIn(signInClick: () -> Unit) {
        Log.d("AuthViewModel", "Starting sign in process")
        viewModelScope.launch {
            setLoading(true)
            try {
                withTimeout(30_000) {
                    Log.d("AuthViewModel", "Executing sign in click")
                    signInClick()
                }
            } catch (e: Exception) {
                Log.e("AuthViewModel", "Error during sign in", e)
                when (e) {
                    is TimeoutCancellationException -> handleError(e)
                    else -> handleError(e)
                }
            } finally {
                setLoading(false)
                Log.d("AuthViewModel", "Sign in process completed")
            }
        }
    }

    fun onSignInResult(result: SignInResult) {
        viewModelScope.launch {
            setLoading(true)
            try {
                processSignIn(result)
            } catch (e: Exception) {
                handleError(e)
            } finally {
                setLoading(false)
            }
        }
    }

    private suspend fun processSignIn(result: SignInResult) {
        if (result.data == null) {
            throw Exception("Sign in failed: No user data")
        }

        try {
            val remoteUser = authenticateUser(result)
            userRepository.saveUser(
                User(
                    firebaseUid = remoteUser.firebaseUid,
                    name = remoteUser.name,
                    email = remoteUser.email
                )
            )
            Log.d(TAG, "Successfully saved user to local database: ${remoteUser.firebaseUid}")

            dataSyncManager.setupPeriodicSync()
            dataSyncManager.requestImmediateSync()

            updateSuccessState(result.data)
        } catch (e: Exception) {
            Log.e(TAG, "Sign in error", e)
            throw e
        }
    }

    private suspend fun authenticateUser(result: SignInResult): UserDTO {
        val userData = result.data!!
        val userDTO = createUserDTO(userData)

        return authApiService.createOrUpdateUser(userDTO)
    }


    private fun createUserDTO(userData: UserData) = UserDTO(
        id = null,
        firebaseUid = userData.firebaseUid,
        name = userData.username ?: "",
        email = userData.email ?: "",
        createdAt = LocalDateTime.now().toString(),
        updatedAt = System.currentTimeMillis()
    )

    private suspend fun updateUser(userDTO: UserDTO, authService: AuthApiService): UserDTO {
        Log.d(TAG, "Starting user update for UID: ${userDTO.firebaseUid}")
        return try {
            val updatedUser = authService.updateUserData(userDTO)
            Log.d(TAG, "Update successful, received: $updatedUser")
            updatedUser
        } catch (e: Exception) {
            when (e) {
                is HttpException -> {
                    Log.e(TAG, "HTTP Error message: ${e.message()}")
                }
                is IOException -> {
                    Log.e(TAG, "Network Error: ${e.message}")
                }
            }
            userDTO
        }
    }

    private suspend fun loadUserData() {
        try {
            groceryListRepository.fetchUserLists()
        } catch (e: Exception) {
            Log.e(TAG, "Error loading user data", e)
        }
    }


    fun signOut() {
        viewModelScope.launch {
            try {
                authClient.signOut()
                dataSyncManager.cancelSync()
                resetState()
            } catch (e: Exception) {
                _state.update {
                    it.copy(signInError = e.message)
                }
                Log.e(TAG, "Error during sign out, ${state.value.signInError}", e)
            }
        }
    }

    private fun updateSuccessState(userData: UserData) {
        preferencesManager.isFirstInstall = false
        _state.update {
            it.copy(
                isSignInSuccessful = true,
                signInError = null,
                isSignedIn = true
            )
        }
    }

    private fun handleError(error: Exception) {
        Log.e(TAG, "Auth error: $error")
        _state.update {
            it.copy(
                isSignInSuccessful = false,
                signInError = error.message,
                isSignedIn = false
            )
        }

        viewModelScope.launch {
            messageHandler.showMessage(AppMessage.Error(error.message ?: "Error occurred"))
        }
    }

    private fun setLoading(isLoading: Boolean) {
        _state.update { it.copy(isLoading = isLoading) }
    }

    fun resetState() {
        val currentUser = authClient.getSignedInUser()
        val isFirstInstall = preferencesManager.isFirstInstall

        _state.update {
            SignInState(
                isSignInSuccessful = false,
                isLoading = false,
                isSignedIn = currentUser != null && !isFirstInstall,
                signInError = null
            )
        }
    }

    fun resetLoadingState() {
        _state.update {
            it.copy(
                isLoading = false,
                signInError = null
            )
        }
    }

    fun clearError() {
        _state.update { it.copy(signInError = null) }
    }

    companion object {
        private const val TAG = "AuthViewModel"
    }
}
