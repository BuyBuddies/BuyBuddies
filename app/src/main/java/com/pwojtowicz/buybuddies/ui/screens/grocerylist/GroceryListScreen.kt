package com.pwojtowicz.buybuddies.ui.screens.grocerylist

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.pwojtowicz.buybuddies.data.enums.MeasurementUnit
import com.pwojtowicz.buybuddies.data.enums.PurchaseStatus
import com.pwojtowicz.buybuddies.ui.components.ContainerCard
import com.pwojtowicz.buybuddies.navigation.NavItems
import com.pwojtowicz.buybuddies.navigation.navigateToScreen
import com.pwojtowicz.buybuddies.ui.screens.grocerylist.groceryitem.GroceryItemRow
import com.pwojtowicz.buybuddies.viewmodel.GroceryViewModel
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun GroceryListScreen(
    groceryListId: Long,
    navController: NavHostController,
    paddingValues: PaddingValues,
    viewModel: GroceryViewModel = hiltViewModel()
) {
    val groceryItemsList by viewModel.groceryListItems.collectAsState()
    val groceryListName by viewModel.groceryListName.collectAsState()
    val members by viewModel.members.collectAsState()
    val showAddGroceryItem = remember { MutableStateFlow(false) }
    val hasUnsavedChanges by viewModel.hasUnsavedChanges
    var showUnsavedChangesDialog by remember { mutableStateOf(false) }

    val completedItems = groceryItemsList.filter { it.listId == groceryListId && it.purchaseStatus.toBoolean() }
    val activeItems = groceryItemsList.filter { it.listId == groceryListId && !it.purchaseStatus.toBoolean() }

    LaunchedEffect(Unit) {
        viewModel.setActiveGroceryListId(groceryListId)
        viewModel.fetchGroceryListName(groceryListId)
        viewModel.fetchMembers(groceryListId)
    }

    // Handle back navigation with unsaved changes
    BackHandler(enabled = hasUnsavedChanges) {
        showUnsavedChangesDialog = true
    }

    Box(modifier = Modifier.padding(paddingValues)) {
        // Add Grocery Item Dialog
        if (showAddGroceryItem.collectAsState().value) {
            AddGroceryItem(
                isVisible = showAddGroceryItem,
                onDismiss = { showAddGroceryItem.value = false },
                onAddItem = { name, quantity, unit ->
                    viewModel.createGroceryItem(
                        listId = groceryListId,
                        name = name,
                        quantity = quantity,
                        unit = unit,
                        status = PurchaseStatus.PENDING
                    )
                }
            )
        }

        if (showUnsavedChangesDialog) {
            UnsavedChangesDialog(
                onConfirm = {
                    viewModel.saveChanges()
                    showUnsavedChangesDialog = false
                    navController.navigateUp()
                },
                onDiscard = {
                    viewModel.discardChanges()
                    showUnsavedChangesDialog = false
                    navController.navigateUp()
                },
                onDismiss = {
                    showUnsavedChangesDialog = false
                }
            )
        }


        Column {
            GroceryListTopContainer(
                listName = groceryListName,
                hasUnsavedChanges = hasUnsavedChanges,
                onSaveClick = { viewModel.saveChanges() },
                onProfileClick = {
                    navigateToScreen(
                        navController = navController,
                        route = NavItems.Profile.route
                    )
                },
                onDeleteList = {
                    viewModel.deleteList(groceryListId)
                    navController.navigateUp()
                },
                onUpdateListName = { newName ->
                    viewModel.updateListName(groceryListId, newName)
                },
                onAddMember = { email ->
                    viewModel.addMember(email)
                },
                onDeleteMember = { email ->
                    viewModel.deleteMember(groceryListId, email)
                },
                members = members
            )

            ContainerCard(
                modifier = Modifier.fillMaxSize(),
                shape = RoundedCornerShape(10.dp, 10.dp, 0.dp, 0.dp),
                contentPadding = 8.dp
            ) {
                LazyColumn {
                    // Active Items Section
                    item {
                        Text(
                            text = "Active Items",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp)
                        )
                    }

                    items(activeItems, key = { it.id }) { groceryItem ->
                        GroceryItemRow(
                            groceryListItem = groceryItem,
                            onCheckedChange = {
                                viewModel.addToUnsavedChanges(groceryItem.copy(purchaseStatus = PurchaseStatus.PURCHASED))
                                viewModel.toggleGroceryItemChecked(groceryItem)
                            },
                            onNameChange = { newName ->
                                viewModel.addToUnsavedChanges(groceryItem.copy(name = newName))
                                viewModel.updateGroceryItemName(groceryItem, newName)
                            },
                            onQuantityChange = { newQuantity ->
                                viewModel.addToUnsavedChanges(
                                    groceryItem.copy(quantity = newQuantity.toDoubleOrNull() ?: 0.0)
                                )
                                viewModel.updateGroceryItemQuantity(groceryItem, newQuantity)
                            },
                            onUnitChange = { newUnit ->
                                viewModel.addToUnsavedChanges(
                                    groceryItem.copy(unit = MeasurementUnit.fromString(newUnit) ?: MeasurementUnit.PIECE)
                                )
                                viewModel.updateGroceryItemUnit(groceryItem, newUnit)
                            },
                            onDelete = { viewModel.deleteGroceryItem(groceryItem) }
                        )
                    }

                    // Completed Items Section
                    if (completedItems.isNotEmpty()) {
                        item {
                            Text(
                                text = "Completed Items",
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp)
                            )
                        }
                        items(completedItems, key = { it.id }) { groceryItem ->
                            GroceryItemRow(
                                groceryListItem = groceryItem,
                                onCheckedChange = {
                                    viewModel.addToUnsavedChanges(groceryItem.copy(purchaseStatus = PurchaseStatus.PENDING))
                                    viewModel.toggleGroceryItemChecked(groceryItem)
                                },
                                onNameChange = { newName ->
                                    viewModel.addToUnsavedChanges(groceryItem.copy(name = newName))
                                    viewModel.updateGroceryItemName(groceryItem, newName)
                                },
                                onQuantityChange = { newQuantity ->
                                    viewModel.addToUnsavedChanges(
                                        groceryItem.copy(quantity = newQuantity.toDoubleOrNull() ?: 0.0))
                                    viewModel.updateGroceryItemQuantity(groceryItem, newQuantity)
                                },
                                onUnitChange = { newUnit ->
                                    viewModel.addToUnsavedChanges(
                                        groceryItem.copy(unit = MeasurementUnit.fromString(newUnit) ?: MeasurementUnit.PIECE))
                                    viewModel.updateGroceryItemUnit(groceryItem, newUnit)
                                },
                                onDelete = { viewModel.deleteGroceryItem(groceryItem) }
                            )
                        }
                    }
                }
            }
        }
        GroceryItemAddFAB(
            onClick = { showAddGroceryItem.value = true },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 32.dp)
                .size(60.dp)
        )
    }
}


@Preview
@Composable
fun PreviewGroceryListScreen() {
    val paddingValues = PaddingValues(30.dp)
    val navController = rememberNavController()

    GroceryListScreen(
        groceryListId = 0,
        navController = navController,
        paddingValues = paddingValues
    )
}