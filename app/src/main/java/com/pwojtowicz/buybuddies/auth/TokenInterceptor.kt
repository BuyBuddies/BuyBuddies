package com.pwojtowicz.buybuddies.auth

import android.util.Log
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import javax.inject.Inject

class TokenInterceptor @Inject constructor(
    private val authClient: AuthorizationClient
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest  = chain.request()

        var response = makeRequestWithToken(chain, originalRequest)

        if (response.code == 401) {
            Log.d("TokenInterceptor", "Got 401, refreshing token and retrying")
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
            authClient.getIdToken(forceRefresh) ?: throw Exception("Failed to get token")
        }

        val newRequest = originalRequest.newBuilder()
            .header("Authorization", "Bearer $token")
            .build()

        return chain.proceed(newRequest)
    }
}