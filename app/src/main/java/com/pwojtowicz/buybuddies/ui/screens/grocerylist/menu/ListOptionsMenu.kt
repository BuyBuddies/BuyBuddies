package com.pwojtowicz.buybuddies.ui.screens.grocerylist.menu

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ListOptionsMenu(
    showMenu: Boolean,
    onShowMenuChange: (Boolean) -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onAddMemberClick: () -> Unit,
    onManageMembersClick: () -> Unit
) {
    Box(
        modifier = Modifier.padding(start = 16.dp, end = 32.dp)
    ) {
        IconButton(
            onClick = { onShowMenuChange(true) }
        ) {
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = "List options",
                tint = Color.White
            )
        }
        DropdownMenu(
            expanded = showMenu,
            onDismissRequest = { onShowMenuChange(false) }
        ) {
            ListMenuItem(
                text = "Edit List Name",
                icon = Icons.Default.Edit,
                onClick = {
                    onShowMenuChange(false)
                    onEditClick()
                }
            )
            ListMenuItem(
                text = "Delete List",
                icon = Icons.Default.Delete,
                onClick = {
                    onShowMenuChange(false)
                    onDeleteClick()
                }
            )
            ListMenuItem(
                text = "Add Member",
                icon = Icons.Default.Add,
                onClick = {
                    onShowMenuChange(false)
                    onAddMemberClick()
                }
            )
            ListMenuItem(
                text = "Manage Members",
                icon = Icons.Default.Person,
                onClick = {
                    onShowMenuChange(false)
                    onManageMembersClick()
                }
            )
        }
    }
}