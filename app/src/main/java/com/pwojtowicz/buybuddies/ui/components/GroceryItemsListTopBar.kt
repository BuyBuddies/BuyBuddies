package com.pwojtowicz.buybuddies.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pwojtowicz.buybuddies.data.entity.GroceryList
import com.pwojtowicz.buybuddies.data.entity.GroceryListStatus
import com.pwojtowicz.buybuddies.ui.theme.DarkSurfaceColor

@Composable
fun CustomTopBar(
    groceryList: GroceryList,
    deleteList: () -> Unit,
    editList: (GroceryList) -> Unit,
    updateAsDone: (GroceryList) -> Unit
) {
    var showMenu by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(45.dp)
            .background(DarkSurfaceColor),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(
            text = groceryList.name,
            modifier = Modifier.padding(start = 16.dp),
            style = TextStyle(
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        )

        Spacer(Modifier.weight(1f))

        Box() {
            IconButton(onClick = { showMenu = true }) {
                Icon(Icons.Default.MoreVert, contentDescription = "Options")
            }
            DropdownMenu(
                expanded = showMenu,
                onDismissRequest = { showMenu = false },
            ) {
                if (groceryList.listStatus == GroceryListStatus.ACTIVE.name) {
                    DropdownMenuItem(
                        text = { Text("Done") },
                        onClick = {
                            updateAsDone(groceryList.copy(listStatus = GroceryListStatus.DONE.name))
                            showMenu = false
                        }
                    )
                } else {
                    DropdownMenuItem(
                        text = { Text("Make Active") },
                        onClick = {
                            updateAsDone(groceryList.copy(listStatus = GroceryListStatus.ACTIVE.name))
                            showMenu = false
                        }
                    )
                }
                DropdownMenuItem(
                    text = { Text("Edit name") },
                    onClick = {
                        showMenu = false
                        showEditDialog = true
                    }
                )
                DropdownMenuItem(
                    text = { Text("Delete list") },
                    onClick = {
                        deleteList()
                        showMenu = false
                    }
                )
            }
            if (showEditDialog) {
                EditGroceryListDialog(
                    groceryList = groceryList,
                    onDismiss = { showEditDialog = false },
                    onSave = { editedList ->
                        editList(editedList)
                        showEditDialog = false
                    }
                )
            }
        }
    }
}

@Composable
fun EditGroceryListDialog(groceryList: GroceryList, onDismiss: () -> Unit, onSave: (GroceryList) -> Unit) {
    var name by remember { mutableStateOf(groceryList.name) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit Item") },
        text = {
            Column {
                TextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name") }
                )
            }
        },
        confirmButton = {
            TextButton(onClick = {
                onSave(groceryList.copy(name = name))
                onDismiss()
            }) {
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