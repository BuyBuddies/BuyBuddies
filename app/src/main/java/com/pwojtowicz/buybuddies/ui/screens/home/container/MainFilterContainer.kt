package com.pwojtowicz.buybuddies.ui.screens.home.container

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pwojtowicz.buybuddies.data.entity.GroceryListLabel
import com.pwojtowicz.buybuddies.data.entity.GroceryListStatus
import com.pwojtowicz.buybuddies.ui.components.BBDropdownMenu
import com.pwojtowicz.buybuddies.ui.components.SearchInputBar

@Composable
fun MainFilterContainer(
    onSearchInput: (String) -> Unit,
    onStatusFilterChange: (GroceryListStatus?) -> Unit,
    onLabelFilterChange: (GroceryListLabel?) -> Unit,
    groceryListLabels: List<GroceryListLabel>
) {
    var searchText by remember { mutableStateOf("") }
    var selectedStatus by remember { mutableStateOf<GroceryListStatus?>(null) }
    var selectedLabel by remember { mutableStateOf<GroceryListLabel?>(null) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 24.dp, start = 12.dp, end = 12.dp)
    ) {
        Row {
            SearchInputBar(
                onSearchInput = { text ->
                    searchText = text
                    onSearchInput(text)
                }
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row {
            BBDropdownMenu(
                selectedValue = selectedStatus?.name ?: "None",
                onValueChange = { status ->
                    selectedStatus = if (status == "None") null else GroceryListStatus.valueOf(status)
                    onStatusFilterChange(selectedStatus)
                },
                options = listOf("None") + GroceryListStatus.values().map { it.name },
                label = "Status",
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            BBDropdownMenu(
                selectedValue = selectedLabel?.name ?: "None",
                onValueChange = { label ->
                    selectedLabel = if (label == "None") null else groceryListLabels.first { it.name == label }
                    onLabelFilterChange(selectedLabel)
                },
                options = listOf("None") + groceryListLabels.map { it.name },
                label = "Type",
                modifier = Modifier.weight(1f)
            )
        }
    }
}
