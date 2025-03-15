package com.pwojtowicz.buybuddies.ui.screens.grocerylist

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pwojtowicz.buybuddies.ui.theme.bb_theme_main_selected_clr

@Composable
fun GroceryItemAddFAB(modifier: Modifier, onClick: () -> Unit) {
    FloatingActionButton(
        modifier = modifier,
        onClick = { onClick() },
        containerColor = bb_theme_main_selected_clr,
        shape = CircleShape
    ) {
        Icon(Icons.Default.Add, contentDescription = "Add Grocery Item Button", modifier = Modifier.size(50.dp))
    }
}
@Preview
@Composable
fun GroceryItemAddFABPreview(){
    val emptyLambda: () -> Unit = {}
    GroceryItemAddFAB(modifier = Modifier, onClick = emptyLambda)
}