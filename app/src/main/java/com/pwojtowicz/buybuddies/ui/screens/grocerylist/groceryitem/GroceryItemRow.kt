package com.pwojtowicz.buybuddies.ui.screens.grocerylist.groceryitem

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pwojtowicz.buybuddies.data.entity.GroceryListItem
import com.pwojtowicz.buybuddies.data.enums.MeasurementUnit
import com.pwojtowicz.buybuddies.ui.components.BBDropdownMenu
import com.pwojtowicz.buybuddies.ui.components.HorizontalScrollableTextField
import com.pwojtowicz.buybuddies.ui.theme.bb_theme_background_clr_light
import com.pwojtowicz.buybuddies.ui.theme.bb_theme_text_clr_dark


@Composable
fun GroceryItemRow(
    groceryListItem: GroceryListItem,
    onCheckedChange: (Boolean) -> Unit,
    onNameChange: (String) -> Unit,
    onQuantityChange: (String) -> Unit,
    onUnitChange: (String) -> Unit,
    onDelete: () -> Unit
) {1
    val unitOptions = MeasurementUnit.values().map { it.toShortString() }
    var showMenu by remember { mutableStateOf(false) }

    Box(modifier = Modifier
        .background(Color.Transparent)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 0.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Checkbox(
                checked = groceryListItem.purchaseStatus.toBoolean(),
                onCheckedChange = onCheckedChange
            )
            Spacer(Modifier.width(8.dp))
            HorizontalScrollableTextField(
                value = groceryListItem.name,
                onValueChange = onNameChange,
                modifier = Modifier.weight(1f)
            )

            Spacer(Modifier.width(8.dp))
            HorizontalScrollableTextField(
                value = groceryListItem.quantity.toString(),
                onValueChange = onQuantityChange,
                modifier = Modifier.width(50.dp)
            )

            Spacer(Modifier.width(8.dp))
            BBDropdownMenu(
                groceryListItem.unit.toShortString(),
                options = unitOptions,
                label = "Unit",
                onValueChange = { newShortUnit  ->
                    MeasurementUnit.values()
                        .find { it.toShortString() == newShortUnit }
                        ?.let { onUnitChange(it.name) }
                },
                textColor = bb_theme_text_clr_dark,
                backgroundColor = bb_theme_background_clr_light,
                dropdownItemColor = bb_theme_text_clr_dark,
                dropdownBackgroundColor = bb_theme_background_clr_light,
                textStyle = TextStyle(fontSize = 14.sp),
                labelStyle = TextStyle(fontSize = 14.sp),
                modifier = Modifier.width(95.dp)
            )

            Box {
                IconButton(
                    onClick = { showMenu = !showMenu },
                    modifier = Modifier.padding(end = 4.dp)
                ) {
                    Icon(
                        Icons.Default.MoreVert,
                        contentDescription = "More Options",
                    )
                }
                DropdownMenu(
                    expanded = showMenu,
                    onDismissRequest = { showMenu = false }
                    ) {
                    DropdownMenuItem(
                        text = { Text("Delete") },
                        onClick = {
                            onDelete()
                            showMenu = false
                        }
                    )
                }
            }
        }
    }
}