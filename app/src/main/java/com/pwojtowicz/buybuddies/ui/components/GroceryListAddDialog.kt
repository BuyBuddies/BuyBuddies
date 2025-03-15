package com.pwojtowicz.buybuddies.ui.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun GroceryListAddDialog(
    onSave: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var listName by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Grocery List") },
        text = {
            TextField(
                value = listName,
                onValueChange = { listName = it },
                placeholder = { Text("List Name") }
            )
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onSave(listName)
                }
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Preview
@Composable
fun GroceryListAddDialogPreview(){
    GroceryListAddDialog(
        onSave = { listName -> {}},
        onDismiss = {}
    )
}