package com.pwojtowicz.buybuddies.ui.message

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.pwojtowicz.buybuddies.viewmodel.MessageViewModel

@Composable
fun AppMessageHandler(
    modifier: Modifier = Modifier
) {
    val messageViewModel: MessageViewModel = hiltViewModel()

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val message by messageViewModel.messageFlow.collectAsStateWithLifecycle(initialValue = null)

    LaunchedEffect(message) {
        message?.let {
            val result = snackbarHostState.showSnackbar(
                message = when (it) {
                    is AppMessage.Error -> it.message
                    is AppMessage.Success -> it.message
                    is AppMessage.Info -> it.message
                },
                duration = SnackbarDuration.Short,
                withDismissAction = true,
                actionLabel = "Dismiss"
            )
        }
    }

    Box(modifier = modifier) {
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
        ) { data ->
            Snackbar(
                snackbarData = data,
                containerColor = when (message) {
                    is AppMessage.Error -> Color.Red.copy(alpha = 0.8f)
                    is AppMessage.Success -> Color.Green.copy(alpha = 0.8f)
                    is AppMessage.Info -> MaterialTheme.colorScheme.primary
                    null -> MaterialTheme.colorScheme.primary
                }
            )
        }
    }
}