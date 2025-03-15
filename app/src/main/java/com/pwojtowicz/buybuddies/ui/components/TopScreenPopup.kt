package com.pwojtowicz.buybuddies.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun TopScreenPopup(message: String, onDismiss: () -> Unit) {
    Box(
        contentAlignment = Alignment.TopCenter,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
    ) {
        Card(
            modifier = Modifier
                .padding(16.dp),
            elevation = CardDefaults.cardElevation()
        ) {
            Row(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = message)
                IconButton(onClick = onDismiss) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Close Popup")
                }
            }
        }
    }
}

@Preview
@Composable
fun TopScreenPopupPreview(){
    TopScreenPopup(message = "PopUpTest", onDismiss = {})
}
