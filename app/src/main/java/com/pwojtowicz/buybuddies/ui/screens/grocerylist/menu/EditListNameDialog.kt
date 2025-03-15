package com.pwojtowicz.buybuddies.ui.screens.grocerylist.menu

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.pwojtowicz.buybuddies.ui.theme.bb_theme_main_color

@Composable
fun EditListNameDialog(
    currentName: String,
    onUpdateName: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var newName by remember { mutableStateOf(currentName) }
    var isError by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color.White,
        title = { Text("Edit List Name", color = Color.Black) },
        text = {
            Column {
                OutlinedTextField(
                    value = newName,
                    onValueChange = {
                        newName = it
                        isError = false
                    },
                    label = { Text("List Name", color = Color.Black) },
                    isError = isError,
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black,
                        focusedLabelColor = bb_theme_main_color,
                        unfocusedLabelColor = Color.Black
                    )
                )
                if (isError) {
                    Text(
                        text = "List name cannot be empty",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                    )
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (newName.isBlank()) {
                        isError = true
                    } else {
                        onUpdateName(newName)
                        onDismiss()
                    }
                }
            ) {
                Text(
                    "Save",
                    color = Color.Black
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    "Cancel",
                    color = Color.Black
                )
            }
        }
    )
}