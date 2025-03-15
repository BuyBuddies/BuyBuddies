package com.pwojtowicz.buybuddies.ui.screens.home.container

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pwojtowicz.buybuddies.data.entity.GroceryList
import com.pwojtowicz.buybuddies.data.entity.GroceryListLabel
import com.pwojtowicz.buybuddies.data.entity.GroceryListStatus
import com.pwojtowicz.buybuddies.ui.components.ContainerCard

@Composable
fun MainListContainer(
    modifier: Modifier,
    scrollProgress: Float,
    groceryLists: List<GroceryList>,
    groceryListLabels: List<GroceryListLabel>,
    onClickGroceryList: (String) -> Unit,
    onLongPressGroceryList: (Long) -> Unit,
    onSearchInput: (String) -> Unit,
    onStatusFilterChange: (GroceryListStatus?) -> Unit,
    onLabelFilterChange: (GroceryListLabel?) -> Unit
) {
    val cornerRadius = (10 * (1 - scrollProgress)).dp

    ContainerCard(
        modifier,
        shape = RoundedCornerShape(cornerRadius, cornerRadius, 0.dp, 0.dp),
        contentPadding = 0.dp
    ) {
        Column {
            MainFilterContainer(
                onSearchInput = onSearchInput,
                onStatusFilterChange = onStatusFilterChange,
                onLabelFilterChange = onLabelFilterChange,
                groceryListLabels = groceryListLabels
            )

            LazyVerticalGrid(
                modifier = Modifier.padding(16.dp, 0.dp),
                columns = GridCells.Fixed(3)
            ) {
                item(span = { GridItemSpan(3) }) {
                    Spacer(modifier = Modifier.height(20.dp))
                }

                items(groceryLists) { groceryList ->
                    GroceryListCard(
                        groceryList = groceryList,
                        modifier = Modifier.pointerInput(groceryList.id) {
                            detectTapGestures(
                                onTap = { onClickGroceryList(groceryList.id.toString()) },
                                onLongPress = {
                                    onLongPressGroceryList(groceryList.id)
                                }
                            )
                        }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMainListContainer() {
    val mockGroceryLists = listOf(
        GroceryList(name = "Weekly Groceries", listStatus = GroceryListStatus.ACTIVE.name),
        GroceryList(name = "Daily Groceries", listStatus = GroceryListStatus.ACTIVE.name),
        GroceryList(name = "Home Groceries", listStatus = GroceryListStatus.ACTIVE.name),
        GroceryList(name = "Dinner Groceries", listStatus = GroceryListStatus.ACTIVE.name),
        GroceryList(name = "Breakfast Groceries", listStatus = GroceryListStatus.ACTIVE.name),
        GroceryList(name = "Lunch Groceries", listStatus = GroceryListStatus.ACTIVE.name),
        GroceryList(name = "Party Supplies", listStatus = GroceryListStatus.DONE.name)
    )

    val mockGroceryListLabels = listOf(
        GroceryListLabel(name = "Urgent"),
        GroceryListLabel(name = "Favorites")
    )

    MainListContainer(
        modifier = Modifier.fillMaxSize(),
        scrollProgress = 0.5f,
        groceryLists = mockGroceryLists,
        groceryListLabels = mockGroceryListLabels,
        onClickGroceryList = { id -> println("Clicked on Grocery List with ID: $id") },
        onLongPressGroceryList = { id -> println("Long pressed on Grocery List with ID: $id") },
        onSearchInput = { input -> println("Search input: $input") },
        onStatusFilterChange = { status -> println("Status filter changed to: $status") },
        onLabelFilterChange = { label -> println("Label filter changed to: $label") }
    )
}

