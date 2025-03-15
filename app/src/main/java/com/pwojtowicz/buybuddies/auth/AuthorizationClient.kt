package com.pwojtowicz.buybuddies.auth

import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.util.Log
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.pwojtowicz.buybuddies.R
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthorizationClient @Inject constructor(
    @ApplicationContext private val context: Context,
    private val oneTapClient: SignInClient
) {
    private val auth = FirebaseAuth.getInstance()

    suspend fun signIn(): IntentSender? {
        Log.d("AuthClient", "Starting sign in process")
        val result = try {
            Log.d("AuthClient", "Building sign in request")
            val request = buildSignInRequest()
            Log.d("AuthClient", "Beginning sign in with request")
            oneTapClient.beginSignIn(request)
                .await()
                .also { Log.d("AuthClient", "Sign in result received: $it") }
        } catch (e: Exception) {
            Log.e("AuthClient", "Error during sign in", e)
            null
        }
        Log.d("AuthClient", "Returning intent sender: ${result?.pendingIntent?.intentSender}")
        return result?.pendingIntent?.intentSender
    }

    private fun buildSignInRequest(): BeginSignInRequest {
        val clientId = context.getString(R.string.web_client_id)
        return BeginSignInRequest.Builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setFilterByAuthorizedAccounts(false)
                    .setServerClientId(clientId)
                    .build()
                    .also { Log.d("AuthClient", "Built token request options") }
            )
            .setAutoSelectEnabled(false)
            .build()
            .also { Log.d("AuthClient", "Built complete sign in request") }
    }

    suspend fun signInWithIntent(intent: Intent): SignInResult {
        val credential = try {
            oneTapClient.getSignInCredentialFromIntent(intent)
        } catch (e: Exception) {
            return createSignInResult(
                user = null,
                error = e.message
            )
        }

        val googleIdToken = credential.googleIdToken
        if (googleIdToken == null) {
            return createSignInResult(
                user = null,
                error = "Google ID token is Null"
            )
        }

        val googleCredentials = GoogleAuthProvider.getCredential(googleIdToken, null)
        return try {
            val result = auth.signInWithCredential(googleCredentials).await()
            createSignInResult(
                user = result.user,
                isNewUser = result.additionalUserInfo?.isNewUser ?: false
            )
        } catch (e: Exception) {
            createSignInResult(
                user = null,
                error = e.message
            )
        }
    }

    private fun createSignInResult(
        user: FirebaseUser?,
        isNewUser: Boolean = false,
        error: String? = null
    ): SignInResult {
        if (user == null) {
            return SignInResult(
                data = null,
                errorMessage = error ?: "Unknown error occured",
                isNewUser = false
            )
        }

        return SignInResult(
            data = UserData(
                firebaseUid = user.uid,
                username = user.displayName,
                email = user.email,
                profilePictureUrl = user.photoUrl?.toString()
            ),
            errorMessage = null,
            isNewUser = isNewUser
        )
    }

    suspend fun signOut() {
        try {
            oneTapClient.signOut().await()
            auth.signOut()
            context.clearFirebasePersistedData()
        } catch (e: Exception) {
            Log.e(TAG, "Error during sign out", e)
        }
    }

    private fun Context.clearFirebasePersistedData() {
        getSharedPreferences("com.google.firebase.auth.api.Store", Context.MODE_PRIVATE)
            .edit()
            .clear()
            .apply()
        getSharedPreferences("com.google.android.gms.signin", Context.MODE_PRIVATE)
            .edit()
            .clear()
            .apply()
        getSharedPreferences("DefaultPreferencesName", Context.MODE_PRIVATE)
            .edit()
            .clear()
            .apply()

        try {
            context.deleteDatabase("webview.db")
            context.deleteDatabase("webviewCache.db")
        } catch (e: Exception) {
            Log.e(TAG, "Error clearing WebView data", e)
        }
    }

    fun getSignedInUser(): UserData? = auth.currentUser?.let { user ->
        Log.d(TAG, "Firebase current user: ${user.email}")

        UserData(
            firebaseUid = user.uid,
            username = user.displayName,
            email = user.email,
            profilePictureUrl = user.photoUrl?.toString()
        )
    }

    suspend fun getIdToken(forceRefresh: Boolean = false): String? {
        return try {
            auth.currentUser?.getIdToken(forceRefresh)?.await()?.token
        } catch (e: Exception) {
            Log.e(TAG, "Error getting id token", e)
            null
        }
    }

    companion object {
        private const val TAG = "AuthorizationClient"
    }
}