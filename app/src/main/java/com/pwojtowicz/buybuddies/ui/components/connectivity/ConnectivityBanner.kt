package com.pwojtowicz.buybuddies.ui.components.connectivity

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun ConnectivityBanner(
    isConnected: Boolean,
    modifier: Modifier = Modifier,
    includeStatusBarPadding: Boolean = true
) {
    if(!isConnected) {
        val bannerModifier = if (includeStatusBarPadding) {
            modifier
                .fillMaxWidth()
                .windowInsetsPadding(WindowInsets.statusBars)
                .background(Color.Red.copy(alpha = 0.7f))
                .padding(8.dp)
        } else {
            modifier
                .fillMaxWidth()
                .background(Color.Red.copy(alpha = 0.7f))
                .padding(8.dp)
        }

        Box(
            modifier = bannerModifier,
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "No internet connection. Working in offline mode.",
                color = Color.White,
                textAlign = TextAlign.Center
            )
        }
    }
}
