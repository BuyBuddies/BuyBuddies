package com.pwojtowicz.buybuddies.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pwojtowicz.buybuddies.data.entity.GroceryListItem
import com.pwojtowicz.buybuddies.data.enums.PurchaseStatus

@Composable
fun GroceryItemRow(
    groceryListItem: GroceryListItem,
    onItemCheckChanged: (GroceryListItem) -> Unit,
    onClickDeleteGroceryItem: (GroceryListItem) -> Unit,
    onClickEditGroceryItem: (GroceryListItem) -> Unit
) {
    var showEditDialog by remember { mutableStateOf(false) }
    var showMenu by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onItemCheckChanged(groceryListItem.copy(purchaseStatus = groceryListItem.purchaseStatus)) },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = groceryListItem.purchaseStatus.toBoolean(),
            onCheckedChange = { purchaseStatus ->
                onItemCheckChanged(groceryListItem.copy(purchaseStatus = PurchaseStatus.fromBoolean(purchaseStatus)))
            }
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = groceryListItem.name, style = MaterialTheme.typography.bodySmall)
        Spacer(modifier = Modifier.weight(1f))
        Text(text = "x${groceryListItem.quantity}")
        Spacer(modifier = Modifier.width(16.dp))


        Box() {
            IconButton(onClick = {
                showMenu = true
            }) {
                Icon(Icons.Default.MoreVert, contentDescription = "Options")
            }
            DropdownMenu(
                expanded = showMenu,
                onDismissRequest = { showMenu = false },
                modifier = Modifier.align(Alignment.TopEnd) // Aligning the menu under the IconButton
            ) {
                DropdownMenuItem(
                    text = { Text("Edit") },
                    onClick = {
                        showEditDialog = true
                        showMenu = false
                    }
                )
                DropdownMenuItem(
                    text = { Text("Delete") },
                    onClick = {
                        onClickDeleteGroceryItem(groceryListItem)
                        showMenu = false
                    }
                )
            }
            if(showEditDialog) {
                EditGroceryItemDialog(
                    groceryListItem = groceryListItem,
                    onDismiss = { showEditDialog = false },
                    onSave = { editedItem ->
                        onClickEditGroceryItem(editedItem)
                        showEditDialog = false
                    }
                )
            }
        }
    }
}

@Composable
fun EditGroceryItemDialog(groceryListItem: GroceryListItem, onDismiss: () -> Unit, onSave: (GroceryListItem) -> Unit) {
    var name by remember { mutableStateOf(groceryListItem.name) }
    var quantity by remember { mutableStateOf(groceryListItem.quantity.toString()) }

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
                TextField(
                    value = quantity,
                    onValueChange = { quantity = it },
                    label = { Text("Quantity") }
                )
            }
        },
        confirmButton = {
            TextButton(onClick = {
                val updatedQuantity = quantity.toDoubleOrNull() ?: 0.0
                onSave(groceryListItem.copy(name = name, quantity = updatedQuantity))
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

@Preview(showBackground = true)
@Composable
fun PreviewGroceryItemRow() {
    GroceryItemRow(
        groceryListItem = GroceryListItem(
            listId = 1L,
            name = "Milk",
            quantity = 1.0
        ),
        onItemCheckChanged = {},
        onClickEditGroceryItem = {},
        onClickDeleteGroceryItem = {}
    )
}