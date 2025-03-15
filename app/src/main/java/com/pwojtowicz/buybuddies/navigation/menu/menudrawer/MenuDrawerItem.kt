package com.pwojtowicz.buybuddies.navigation.menu.menudrawer

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.DrawerState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.pwojtowicz.buybuddies.navigation.NavItems
import kotlinx.coroutines.CoroutineScope

@Composable
fun MenuDrawerItem(
    navController: NavHostController,
    navItem: NavItems,
    drawerState: DrawerState,
    coroutineScope: CoroutineScope
) {
    MenuDrawerDivider()

    DrawerButton(navItem.title, navItem.icon) {
        navigateToScreenFromDrawer(
            navController,
            navItem.route,
            drawerState,
            coroutineScope
        )
    }
}

@Composable
fun DrawerButton(text: String, icon: ImageVector, onClick: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp)
    ) {
        Icon(icon, contentDescription = text)
        Spacer(Modifier.width(16.dp))
        Text(text)
    }
}