package com.pwojtowicz.buybuddies.navigation

import com.pwojtowicz.buybuddies.ui.screens.auth.LoginScreen
import android.app.Activity.RESULT_OK
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import com.pwojtowicz.buybuddies.navigation.menu.BottomMenu
import com.pwojtowicz.buybuddies.navigation.menu.menudrawer.MenuDrawer
import com.pwojtowicz.buybuddies.ui.components.connectivity.ConnectivityBanner
import com.pwojtowicz.buybuddies.ui.screens.depots.DepotsScreen
import com.pwojtowicz.buybuddies.ui.screens.grocerylist.GroceryListScreen
import com.pwojtowicz.buybuddies.ui.screens.home.HomeScreen
import com.pwojtowicz.buybuddies.ui.screens.homes.HomesScreen
import com.pwojtowicz.buybuddies.ui.screens.notifications.NotificationScreen
import com.pwojtowicz.buybuddies.ui.screens.profile.ProfileScreen
import com.pwojtowicz.buybuddies.ui.screens.scanner.ScannerScreen
import com.pwojtowicz.buybuddies.ui.screens.settings.SettingsScreen
import com.pwojtowicz.buybuddies.viewmodel.AuthViewModel
import com.pwojtowicz.buybuddies.viewmodel.ConnectivityViewModel
import kotlinx.coroutines.launch

/**
 * Root composable for the application's navigation.
 * It observes authentication and connectivity states to determine the appropriate
 * navigation graph (Auth or Main) and displays relevant UI like loading indicators
 * or connectivity banners.
 *
 * @param navController The [NavHostController] used to manage navigation.
 */
@Composable
fun Navigation(
    navController: NavHostController = rememberNavController()
) {
    val authViewModel: AuthViewModel = hiltViewModel()
    val connectivityViewModel: ConnectivityViewModel = hiltViewModel()

    val signInState by authViewModel.state.collectAsStateWithLifecycle()
    val isConnected by connectivityViewModel.isConnected.collectAsState()

    // Log initial authentication state for easier debugging during development.
    LaunchedEffect(Unit) {
        Log.d("Navigation", "Initial SignInState: isSignedIn=${signInState.isSignedIn}, isLoading=${signInState.isLoading}")
    }

    // Display a loading indicator while checking the current authentication status.
    if (signInState.isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    Column(modifier = Modifier.fillMaxSize()) {
        ConnectivityBanner(isConnected = isConnected)

        NavHost(
            navController = navController,
            // Start with authentication flow or main app content based on sign-in status.
            startDestination = if(signInState.isSignedIn) NavRoute.Main.route else NavRoute.Auth.route
        ) {
            authNavigation(navController)
            mainNavigation(navController)
        }
    }
}

/**
 * Defines the navigation graph for authentication-related screens.
 * This includes Login, Registration, and Forgot Password flows.
 *
 * @param navController The [NavHostController] for navigating between auth screens.
 */
private fun NavGraphBuilder.authNavigation(
    navController: NavHostController
) {
    navigation(
        startDestination = NavItems.Login.route,
        route = NavRoute.Auth.route
    ) {
        composable(NavItems.Login.route) {
            AuthContent(navController = navController)
        }
        composable(NavItems.Register.route) { /* TODO: Implement Register Screen */ }
        composable(NavItems.ForgotPassword.route) { /* TODO: Implement Forgot Password Screen */ }
    }
}

/**
 * Defines the navigation graph for the main application screens accessible after successful authentication.
 *
 * @param navController The [NavHostController] for navigating between main app screens.
 */
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

        // Route for displaying a specific grocery list, identified by its ID.
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
            MainContent(navController = navController) { SettingsScreen() }
        }

        composable(NavItems.Notification.route) {
            MainContent(navController = navController) { NotificationScreen() }
        }

        composable(NavItems.Scanner.route) {
            MainContent(navController = navController) { ScannerScreen() }
        }
        composable(NavItems.Depot.route) {
            MainContent(navController = navController) { DepotsScreen() }
        }
        composable(NavItems.Home.route) { // Consider renaming if this is distinct from the main NavItems.Main route
            MainContent(navController = navController) { HomesScreen() }
        }
    }
}

/**
 * Composable responsible for rendering the authentication UI (currently LoginScreen)
 * and handling the sign-in process, including Google Sign-In and guest mode.
 *
 * @param navController The [NavHostController] to navigate upon successful authentication.
 * @param authViewModel The [AuthViewModel] managing authentication state and logic.
 */
@Composable
fun AuthContent(
    navController: NavHostController,
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val signInState by authViewModel.state.collectAsStateWithLifecycle()
    val coroutineScope = rememberCoroutineScope()

    // Activity result launcher for Google Sign-In intent.
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult(),
        onResult = { result ->
            if(result.resultCode == RESULT_OK) {
                coroutineScope.launch {
                    val signInResult = authViewModel.signInWithIntent(result.data ?: return@launch)
                    authViewModel.onSignInResult(signInResult)
                }
            }
            else {
                // Reset loading state if sign-in was cancelled or failed.
                authViewModel.resetLoadingState()
            }
        }
    )

    // Navigate to main app content upon successful sign-in.
    LaunchedEffect(key1 = signInState.isSignInSuccessful) {
        if(signInState.isSignInSuccessful){
            Toast.makeText(context, "Sign in successful", Toast.LENGTH_LONG).show()
            navController.navigate(NavRoute.Main.route){
                popUpTo(NavRoute.Auth.route) { inclusive = true } // Clear auth back stack
            }
            authViewModel.resetState() // Clean up auth state after navigation
        }
    }

    // Navigate to main app content if guest mode is enabled.
    LaunchedEffect(key1 = signInState.isGuestMode) {
        if (signInState.isGuestMode) {
            navController.navigate(NavRoute.Main.route){
                popUpTo(NavRoute.Auth.route) { inclusive = true } // Clear auth back stack
            }
            // Note: authViewModel.resetState() might also be relevant here if guest mode should clear prior auth attempts.
        }
    }


    LoginScreen(
        state = signInState,
        onSignInClick = {
            authViewModel.startSignIn { // Sets isLoading state
                coroutineScope.launch {
                    // Attempt to get the sign-in intent and launch it.
                    val signInIntentLauncher = authViewModel.signIn()
                    launcher.launch(
                        IntentSenderRequest.Builder(
                            signInIntentLauncher ?: return@launch // Early return if intent is null
                        ).build()
                    )
                }
            }
        },
        onCleanError = { authViewModel.clearError() },
        onSkipClick = {
            authViewModel.setGuestMode(true)
            Toast.makeText(context, "Continuing as guest", Toast.LENGTH_SHORT).show()
        }
    )
}

/**
 * A common layout wrapper for screens within the main application navigation graph.
 * It provides a [ModalNavigationDrawer] and a [Scaffold] with a [BottomMenu].
 *
 * @param navController The [NavHostController] for navigation actions from the drawer or bottom menu.
 * @param content The composable content of the specific screen to be displayed within this layout.
 */
@Composable
private fun MainContent(
    navController: NavHostController,
    content: @Composable (PaddingValues) -> Unit,
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


/**
 * Navigates to the specified [route], optionally appending arguments.
 * This function configures common navigation options like popping up to the start destination,
 * ensuring a single top instance, and restoring state.
 *
 * @param navController The [NavHostController] to perform the navigation.
 * @param route The base destination route string.
 * @param args A map of arguments to append to the route. Values from this map are appended as path segments.
 *             Example: `navigateToScreen(nav, "userProfile", mapOf("userId" to "123"))` navigates to `userProfile/123`.
 */
fun navigateToScreen(
    navController: NavHostController,
    route: String,
    args: Map<String, String> = emptyMap()
) {
    val argRoute = buildString {
        append(route)
        if (args.isNotEmpty()) {
            // This implementation appends only the values of the arguments.
            // For named arguments in routes like "profile/{userId}", ensure the `route` string
            // itself contains the placeholders and `args` provide the values for replacement
            // or construct the path more carefully here if keys are also part of the path.
            args.forEach { (_, value) -> // Key is not used in current appending logic
                append("/$value")
            }
        }
    }

    navController.navigate(argRoute) {
        // Pop up to the start destination of the current navigation graph to avoid a deep back stack.
        popUpTo(navController.graph.findStartDestination().id) {
            saveState = true
        }
        // Avoid multiple copies of the same destination when re-navigating.
        launchSingleTop = true
        // Restore state when navigating back to this destination.
        restoreState = true
    }
}
