package com.pwojtowicz.buybuddies.ui.screens.grocerylist.menu

import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun ListMenuItem(
    text: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    DropdownMenuItem(
        text = { Text(text) },
        onClick = onClick,
        leadingIcon = { Icon(icon, contentDescription = null) }
    )
}