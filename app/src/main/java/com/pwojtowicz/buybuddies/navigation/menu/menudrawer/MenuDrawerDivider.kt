package com.pwojtowicz.buybuddies.navigation.menu.menudrawer

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Modifier

@Composable
fun MenuDrawerDivider(){
    HorizontalDivider(
        color = Color.White,
        thickness = 1.dp,
        modifier = Modifier.padding(horizontal = 16 .dp)
    )
}