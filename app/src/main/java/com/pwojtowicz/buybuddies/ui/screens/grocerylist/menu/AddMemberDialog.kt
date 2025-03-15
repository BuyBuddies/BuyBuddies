package com.pwojtowicz.buybuddies.ui.screens.grocerylist.menu

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import com.pwojtowicz.buybuddies.ui.theme.bb_theme_main_color

@Composable
fun AddMemberDialog(
    onDismiss: () -> Unit,
    onAddMember: (String) -> Unit
) {
    var email by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color.White,
        title = { Text("Add Member", color = Color.Black) },
        text = {
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email", color = Color.Black) },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email
                ),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,
                    focusedLabelColor = bb_theme_main_color,
                    unfocusedLabelColor = Color.Black
                )
            )
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (email.isNotEmpty()) {
                        onAddMember(email)
                        onDismiss()
                    }
                }
            ) {
                Text("Add", color = Color.Black)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = Color.Black)
            }
        }
    )
}