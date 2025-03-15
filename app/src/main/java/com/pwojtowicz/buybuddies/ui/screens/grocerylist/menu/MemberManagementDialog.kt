package com.pwojtowicz.buybuddies.ui.screens.grocerylist.menu

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun MemberManagementDialog(
    members: List<String>,
    onDismiss: () -> Unit,
    onDeleteMember: (String) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color.White,
        title = { Text("List Members", color = Color.Black) },
        text = {
            LazyColumn {
                items(members) { email ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = email,
                            color = Color.Black
                        )
                        IconButton(onClick = { onDeleteMember(email) }) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete member",
                                tint = Color.Black
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Close", color = Color.Black)
            }
        }
    )
}