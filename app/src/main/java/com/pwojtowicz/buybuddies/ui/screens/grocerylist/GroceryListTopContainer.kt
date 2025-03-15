package com.pwojtowicz.buybuddies.ui.screens.grocerylist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pwojtowicz.buybuddies.R
import com.pwojtowicz.buybuddies.ui.components.user.SideUserProfile
import com.pwojtowicz.buybuddies.ui.screens.grocerylist.menu.AddMemberDialog
import com.pwojtowicz.buybuddies.ui.screens.grocerylist.menu.DeleteConfirmationDialog
import com.pwojtowicz.buybuddies.ui.screens.grocerylist.menu.EditListNameDialog
import com.pwojtowicz.buybuddies.ui.screens.grocerylist.menu.ListOptionsMenu
import com.pwojtowicz.buybuddies.ui.screens.grocerylist.menu.MemberManagementDialog
//import com.pwojtowicz.buybuddies.ui.screens.grocerylist.menu.SaveButton

@Composable
fun GroceryListTopContainer(
    listName: String,
    hasUnsavedChanges: Boolean,
    onSaveClick: () -> Unit,
    onProfileClick: () -> Unit,
    onDeleteList: () -> Unit,
    onUpdateListName: (String) -> Unit,
    onAddMember: (String) -> Unit,
    onDeleteMember: (String) -> Unit,
    members: List<String>
) {
    val height = 100.dp

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp, 10.dp, 0.dp, 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        SideUserProfile(height = height, onProfileClick)
        Spacer(modifier = Modifier.weight(1f))
        ListNameBox(
            listName = listName,
            height = height,
            hasUnsavedChanges = hasUnsavedChanges,
            onSaveClick = onSaveClick,
            onDeleteList = onDeleteList,
            onUpdateListName = onUpdateListName,
            onAddMember = onAddMember,
            onDeleteMember = onDeleteMember,
            members = members
        )
        Spacer(modifier = Modifier.weight(0.5f))
    }
}

@Composable
private fun ListNameBox(
    listName: String,
    height: Dp,
    hasUnsavedChanges: Boolean,
    onSaveClick: () -> Unit,
    onDeleteList: () -> Unit,
    onUpdateListName: (String) -> Unit,
    onAddMember: (String) -> Unit,
    onDeleteMember: (String) -> Unit,
    members: List<String>
) {
    var showMenu by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }
    var showAddMemberDialog by remember { mutableStateOf(false) }
    var showMembersDialog by remember { mutableStateOf(false) }
    var showDeleteConfirmation by remember { mutableStateOf(false) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        ListNameDisplay(listName = listName, height = height)

//        if (hasUnsavedChanges) {
//            SaveButton(onSaveClick = onSaveClick)
//        }

        ListOptionsMenu(
            showMenu = showMenu,
            onShowMenuChange = { showMenu = it },
            onEditClick = { showEditDialog = true },
            onDeleteClick = { showDeleteConfirmation = true },
            onAddMemberClick = { showAddMemberDialog = true },
            onManageMembersClick = { showMembersDialog = true }
        )
    }

    // Dialogs
    if (showDeleteConfirmation) {
        DeleteConfirmationDialog(
            onConfirm = {
                onDeleteList()
                showDeleteConfirmation = false
            },
            onDismiss = { showDeleteConfirmation = false }
        )
    }

    if (showEditDialog) {
        EditListNameDialog(
            currentName = listName,
            onUpdateName = onUpdateListName,
            onDismiss = { showEditDialog = false }
        )
    }

    if (showAddMemberDialog) {
        AddMemberDialog(
            onAddMember = onAddMember,
            onDismiss = { showAddMemberDialog = false }
        )
    }

    if (showMembersDialog) {
        MemberManagementDialog(
            members = members,
            onDeleteMember = onDeleteMember,
            onDismiss = { showMembersDialog = false }
        )
    }
}

@Composable
private fun ListNameDisplay(listName: String, height: Dp) {
    val museoModernoRegular = FontFamily(Font(R.font.museo_moderno_regular))

    Box(
        modifier = Modifier
            .height(height).
            widthIn(max = 200.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = listName,
                style = TextStyle(
                    fontFamily = museoModernoRegular,
                    fontSize = 32.sp,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                )
            )
            ListNameUnderlining(modifier = Modifier.padding())
        }
    }
}


@Composable
fun ListNameUnderlining(modifier: Modifier){
    HorizontalDivider(
        color = MaterialTheme.colorScheme.onPrimaryContainer,
        thickness = 1.dp,
        modifier = modifier
    )
}


@Composable
fun UnsavedChangesDialog(
    onConfirm: () -> Unit,
    onDiscard: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Unsaved Changes") },
        text = { Text("You have unsaved changes. What would you like to do?") },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("Save", color = Color.White)
            }
        },
        dismissButton = {
            Row {
                TextButton(onClick = onDiscard) {
                    Text("Discard", color = Color.White)
                }
                TextButton(onClick = onDismiss) {
                    Text("Cancel", color = Color.White)
                }
            }
        }
    )
}

@Preview
@Composable
fun GroceryListTopContainerPreview(){
    GroceryListTopContainer("List Name", true, {}, {}, {}, {}, {}, {}, listOf(""))
}