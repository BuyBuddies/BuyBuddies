package com.pwojtowicz.buybuddies.auth

import android.util.Log
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import javax.inject.Inject

class TokenInterceptor @Inject constructor(
    private val authClient: AuthorizationClient,
    private val guestModeManager: GuestModeManager
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest  = chain.request()

        // For guest user proceed without adding auth token
        if (guestModeManager.isGuestMode()) {
            Log.d(TAG, "User in guest mode, proceeding without token")
            return chain.proceed(originalRequest)
        }

        var response = makeRequestWithToken(chain, originalRequest)

        if (response.code == 401) {
            Log.d(TAG, "Got 401, refreshing token and retrying")
            response.close()
            response = makeRequestWithToken(chain, originalRequest, forceRefresh = true)
        }

        return response
    }

    private fun makeRequestWithToken(
        chain: Interceptor.Chain,
        originalRequest: Request,
        forceRefresh: Boolean = false
    ): Response {
        val token = runBlocking {
            try {
                authClient.getIdToken(forceRefresh) ?: throw Exception("Failed to get token")
            } catch (e: Exception) {
                Log.e(TAG, "Failed to get token", e)
                null
            }
        }

        val newRequest = if (token != null) {
            originalRequest.newBuilder()
                .header("Authorization", "Bearer $token")
                .build()
        } else {
            originalRequest
        }

        return chain.proceed(newRequest)
    }

    companion object {
        private const val TAG = "TokenInterceptor"
    }
}