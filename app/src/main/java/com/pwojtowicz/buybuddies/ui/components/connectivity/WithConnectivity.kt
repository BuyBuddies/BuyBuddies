package com.pwojtowicz.buybuddies.ui.components.connectivity

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.pwojtowicz.buybuddies.viewmodel.ConnectivityViewModel

@Composable
fun WithConnectivity(
    viewModel: ConnectivityViewModel = hiltViewModel(),
    content: @Composable (isConnected: Boolean) -> Unit
) {
    val isConnected by viewModel.isConnected.collectAsState()
    content(isConnected)
}