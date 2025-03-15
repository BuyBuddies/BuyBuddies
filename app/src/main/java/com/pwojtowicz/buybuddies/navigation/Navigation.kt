package com.pwojtowicz.buybuddies.navigation

import LoginScreen
import android.app.Activity.RESULT_OK
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.pwojtowicz.buybuddies.data.network.sync.DataSyncManager
import com.pwojtowicz.buybuddies.navigation.menu.BottomMenu
import com.pwojtowicz.buybuddies.navigation.menu.menudrawer.MenuDrawer
import com.pwojtowicz.buybuddies.ui.screens.depots.DepotsScreen
import com.pwojtowicz.buybuddies.ui.screens.grocerylist.GroceryListScreen
import com.pwojtowicz.buybuddies.ui.screens.home.HomeScreen
import com.pwojtowicz.buybuddies.ui.screens.homes.HomesScreen
import com.pwojtowicz.buybuddies.ui.screens.notifications.NotificationScreen
import com.pwojtowicz.buybuddies.ui.screens.profile.ProfileScreen
import com.pwojtowicz.buybuddies.ui.screens.scanner.ScannerScreen
import com.pwojtowicz.buybuddies.ui.screens.settings.SettingsScreen
import com.pwojtowicz.buybuddies.viewmodel.AuthViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@Composable
fun Navigation(
    navController: NavHostController = rememberNavController()
) {
    val authViewModel: AuthViewModel = hiltViewModel()
    val signInState by authViewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        Log.d("Navigation", "SignInState: isSignedIn=${signInState.isSignedIn}, isLoading=${signInState.isLoading}")
    }

    if (signInState.isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    NavHost(
        navController = navController,
        startDestination = if(signInState.isSignedIn) NavRoute.Main.route else NavRoute.Auth.route
    ) {
        authNavigation(navController)
        mainNavigation(navController)
    }
}

private fun NavGraphBuilder.authNavigation(
    navController: NavHostController
) {
    navigation(
        startDestination = NavItems.Login.route,
        route = NavRoute.Auth.route
    ) {
        composable(NavItems.Login.route) {
            AuthContent(
                navController = navController
            )
        }
        composable(NavItems.Register.route) { }
        composable(NavItems.ForgotPassword.route) { }
    }
}

private fun NavGraphBuilder.mainNavigation(
    navController: NavHostController
) {
    navigation(
        startDestination = NavItems.Main.route,
        route = NavRoute.Main.route
    ) {
        composable(NavItems.Main.route) {
            MainContent(
                navController = navController
            ) { paddingValues ->
                HomeScreen(
                    paddingValues = paddingValues,
                    navController = navController
                )
            }
        }

        composable("${NavItems.GroceryList.route}/{groceryListId}") { backStackEntry ->
            val groceryListId = backStackEntry.arguments?.getString("groceryListId") ?: ""
            MainContent(
                navController = navController
            ) { paddingValues ->
                GroceryListScreen(
                    groceryListId = groceryListId.toLong(),
                    paddingValues = paddingValues,
                    navController = navController
                )
            }
        }

        composable(NavItems.Profile.route) {
            MainContent(
                navController = navController
            ) { paddingValues ->
                ProfileScreen(
                    paddingValues = paddingValues,
                    navController = navController
                )
            }
        }

        composable(NavItems.Settings.route) {
            MainContent(
                navController = navController
            ) {
                SettingsScreen()
            }
        }

        composable(NavItems.Notification.route) {
            MainContent(
                navController = navController
            ) {
                NotificationScreen()
            }
        }

        composable(NavItems.Scanner.route) {
            MainContent(
                navController = navController
            ) {
                ScannerScreen()
            }
        }
        composable(NavItems.Depot.route) {
            MainContent(
                navController = navController
            ) {
                DepotsScreen()
            }
        }
        composable(NavItems.Home.route) {
            MainContent(
                navController = navController
            ) {
                HomesScreen()
            }
        }
    }
}

@Composable
fun AuthContent(
    navController: NavHostController,
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val signInState by authViewModel.state.collectAsStateWithLifecycle()
    val coroutineScope = rememberCoroutineScope()

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult(),
        onResult = { result ->
            if(result.resultCode == RESULT_OK) {
                coroutineScope.launch {
                    val signInResult = authViewModel.signInWithIntent(result.data ?: return@launch)
                    authViewModel.onSignInResult(signInResult)
                }
            }
            else{
                authViewModel.resetLoadingState()
            }
        }
    )

    LaunchedEffect(key1 = signInState.isSignInSuccessful) {
        if(signInState.isSignInSuccessful){
            Toast.makeText(context, "Sign in successful", Toast.LENGTH_LONG).show()
            navController.navigate(NavRoute.Main.route){
                popUpTo(NavRoute.Auth.route) { inclusive = true }
            }
            authViewModel.resetState()
        }
    }

    LoginScreen(
        state = signInState,
        onSignInClick = {
            authViewModel.startSignIn {
                coroutineScope.launch {
                    val signInIntentLauncher = authViewModel.signIn()
                    launcher.launch(
                        IntentSenderRequest.Builder(
                            signInIntentLauncher ?: return@launch
                        ).build()
                    )
                }
            }
        },
        onCleanError = { authViewModel.clearError() }
    )
}

@Composable
private fun MainContent(
    navController: NavHostController,
    content: @Composable (PaddingValues) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            MenuDrawer(
                navController = navController,
                drawerState = drawerState
            )
        }
    ) {
        Scaffold(
            bottomBar = {
                BottomMenu(
                    navController = navController,
                    coroutineScope = coroutineScope,
                    drawerState = drawerState
                )
            }
        ) { paddingValues ->
            content(paddingValues)
        }
    }
}


fun navigateToScreen(
    navController: NavHostController,
    route: String,
    args: Map<String, String> = emptyMap()
) {
    val argRoute = buildString {
        append(route)
        if (args.isNotEmpty()) {
            args.forEach { (value) ->
                append("/$value")
            }
        }
    }

    navController.navigate(argRoute) {
        popUpTo(navController.graph.findStartDestination().id) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}