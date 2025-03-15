package com.pwojtowicz.buybuddies.ui.screens.grocerylist

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.pwojtowicz.buybuddies.ui.theme.bb_theme_card_clr_light

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroceryListMenuSheet(
    isVisible: Boolean,
    onDismiss: () -> Unit,
    onEdit: () -> Unit,
    onMarkAsDone: () -> Unit,
    onDelete: () -> Unit,
    onShare: () -> Unit
) {
    if (isVisible) {
        ModalBottomSheet(
            containerColor = bb_theme_card_clr_light,
            scrimColor = Color(0x33000000),
            onDismissRequest = onDismiss
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                MenuAction(
                    text = "Edit Name",
                    onClick = {
                        onEdit()
                        onDismiss()
                    }
                )

                MenuAction(
                    text = "Mark as Done",
                    onClick = {
                        onMarkAsDone()
                        onDismiss()
                    }
                )

                MenuAction(
                    text = "Delete",
                    onClick = {
                        onDelete()
                        onDismiss()
                    }
                )

                MenuAction(
                    text = "Share",
                    onClick = {
                        onShare()
                        onDismiss()
                    }
                )
            }
        }
    }
}

@Composable
private fun MenuAction(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    TextButton(
        onClick = onClick,
        modifier = modifier.fillMaxWidth()
    ) {
        Text(text)
    }
}