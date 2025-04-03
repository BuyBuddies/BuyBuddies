package com.pwojtowicz.buybuddies.viewmodel

import androidx.lifecycle.ViewModel
import com.pwojtowicz.buybuddies.data.network.NetworkConnectivityManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class ConnectivityViewModel @Inject constructor(
    private val networkConnectivityManager: NetworkConnectivityManager
) : ViewModel() {

    val isConnected: StateFlow<Boolean> = networkConnectivityManager.isConnected

    fun isCurrentlyConnected(): Boolean = networkConnectivityManager.isCurrentlyConnected()
}