package com.pwojtowicz.buybuddies.ui.screens.home

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
fun GroceryListAddFAB(modifier: Modifier, onClick: () -> Unit) {
    FloatingActionButton(
        modifier = modifier,
        onClick = { onClick() },
        containerColor = bb_theme_main_selected_clr,
        shape = CircleShape
    ) {
        Icon(Icons.Default.Add, "Add Grocery List Button", modifier = Modifier.size(50.dp))
    }
}

@Preview
@Composable
fun GroceryListAddFABPreview(){
    val emptyLambda: () -> Unit = {}
    GroceryListAddFAB(modifier = Modifier, onClick = emptyLambda)
}