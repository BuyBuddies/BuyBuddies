package com.pwojtowicz.buybuddies.navigation.menu

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults.colors
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.pwojtowicz.buybuddies.navigation.NavItems
import com.pwojtowicz.buybuddies.navigation.navigateToScreen
import com.pwojtowicz.buybuddies.ui.theme.BuyBuddiesTheme
import com.pwojtowicz.buybuddies.ui.theme.bb_theme_main_color
import com.pwojtowicz.buybuddies.ui.theme.bb_theme_main_selected_clr
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun BottomMenu(navController: NavHostController, coroutineScope: CoroutineScope, drawerState: DrawerState){
    val screens = listOf(
        NavItems.Main,
        NavItems.Scanner,
        NavItems.Notification
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination


    NavigationBar(
        modifier = Modifier.height(100.dp)
            .border(0.25.dp, Color.Black),
        containerColor = bb_theme_main_color,
    ){
        NavigationBarItem(
            icon = { Icon(
                modifier = Modifier.height(40.dp),
                imageVector = Icons.Default.Menu,
                contentDescription = null
            ) },
            selected = false,
            onClick = {
                coroutineScope.launch {
                    drawerState.open()
                }
            }
        )
        screens.forEach{screen ->
            NavigationBarItem(
                icon = { Icon(
                    imageVector = screen.icon,
                    contentDescription = "icon",
                    modifier = Modifier.height(40.dp)
                ) },
                selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                colors = colors(indicatorColor = bb_theme_main_selected_clr),
                onClick = {
                    if (currentDestination?.route != screen.route) {
                        navigateToScreen(
                            navController = navController,
                            route = screen.route
                        )
                    }
                }
            )
        }
    }
}


@Preview
@Composable
fun BottomMenuPreview() {
    val navController = rememberNavController()
    val coroutineScope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    NavHost(navController, startDestination = NavItems.Main.route) {
        composable(NavItems.Main.route) {}
        composable(NavItems.GroceryList.route) {}
        composable(NavItems.Settings.route) {}
    }

    BuyBuddiesTheme {
        Surface {
            BottomMenu(navController, coroutineScope, drawerState)
        }
    }
}