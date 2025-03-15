package com.pwojtowicz.buybuddies.ui.screens.grocerylist

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroceryListCreationCard(
    isVisible: Boolean,
    onDismiss: () -> Unit,
    onCreateList: (String, String) -> Unit
) {
    var showBottomSheet by remember { mutableStateOf(isVisible) }
    var listName by remember { mutableStateOf("") }
    var selectedState by remember { mutableStateOf("") } // Or use an enum for states

    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = {
                showBottomSheet = false
                onDismiss()
            }
        ) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "New Grocery List",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                OutlinedTextField(
                    value = listName,
                    onValueChange = { listName = it },
                    label = { Text("List Name") },
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                )
                // Add Dropdown or Radio Buttons for State selection here

                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        onClick = {
                            showBottomSheet = false
                            onCreateList(listName, selectedState)
                        },
                        modifier = Modifier.padding(8.dp)
                    ) {
                        Text("Create List")
                    }
                    OutlinedButton(
                        onClick = {
                            showBottomSheet = false
                            onDismiss()
                        },
                        modifier = Modifier.padding(8.dp)
                    ) {
                        Text("Cancel")
                    }
                }
            }
        }
    }
}