package com.pwojtowicz.buybuddies.navigation.menu.menudrawer


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.pwojtowicz.buybuddies.navigation.NavItems
import com.pwojtowicz.buybuddies.navigation.NavRoute
import com.pwojtowicz.buybuddies.navigation.navigateToScreen
import com.pwojtowicz.buybuddies.ui.theme.bb_theme_main_color
import com.pwojtowicz.buybuddies.viewmodel.AuthViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun MenuDrawer(
    navController: NavHostController,
    drawerState: DrawerState
) {
    val authViewModel: AuthViewModel = hiltViewModel()
    val coroutineScope = rememberCoroutineScope()

    Surface(
        modifier = Modifier
            .fillMaxWidth(fraction = 0.5f)
            .fillMaxHeight()
            .padding(top = 30.dp),
        color = bb_theme_main_color,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            MenuDrawerProfile(
                onLogout = {
                    authViewModel.signOut()

                    navController.navigate(NavRoute.Auth.route) {
                        popUpTo(NavRoute.Main.route) { inclusive = true }
                    }

                },
                onNavToProfile = {
                    navigateToScreenFromDrawer(
                        navController,
                        NavItems.Profile.route,
                        drawerState,
                        coroutineScope
                    )
                }
            )
            MenuDrawerItem(
                navItem = NavItems.Home,
                navController = navController,
                drawerState = drawerState,
                coroutineScope = coroutineScope
            )
            MenuDrawerItem(
                navItem = NavItems.Type,
                navController = navController,
                drawerState = drawerState,
                coroutineScope = coroutineScope
            )
            MenuDrawerItem(
                navItem = NavItems.Depot,
                navController = navController,
                drawerState = drawerState,
                coroutineScope = coroutineScope
            )

            MenuDrawerItem(
                navItem = NavItems.Settings,
                navController = navController,
                drawerState = drawerState,
                coroutineScope = coroutineScope
            )
            MenuDrawerItem(
                navItem = NavItems.Info,
                navController = navController,
                drawerState = drawerState,
                coroutineScope = coroutineScope
            )
        }
    }
}



fun navigateToScreenFromDrawer(
    navController: NavHostController,
    route: String,
    drawerState: DrawerState,
    coroutineScope: CoroutineScope
) {
    navigateToScreen(
        navController = navController,
        route = route
    )
    coroutineScope.launch {
        drawerState.close()
    }
}


@Preview(showBackground = true, widthDp = 200)
@Composable
fun MenuDrawerPreview() {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    MaterialTheme {
        Surface {
            MenuDrawer(
                navController,
                drawerState
            )
        }
    }
}