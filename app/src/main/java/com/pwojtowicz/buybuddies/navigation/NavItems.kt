package com.pwojtowicz.buybuddies.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed class NavItems(
    val route: String,
    val title: String,
    val icon: ImageVector
) {

    object Menu : NavItems("menu","Menu", Icons.Default.Menu)
    object Home : NavItems("home_screen","Home", Icons.Default.Home)
    object Main : NavItems("main_screen","Main", Icons.Default.Home)
    object Notification : NavItems("notification_screen", "Notification", Icons.Default.Notifications)
    object Scanner : NavItems("scanner_screen", "Scanner", Icons.Default.Search )
    object GroceryList : NavItems("grocery_list_screen", "GroceryList", Icons.Default.Add)
    object Type : NavItems("type_screen", "Type", Icons.Default.List)
    object Depot : NavItems("depot_screen", "Depot", Icons.Default.Menu)
    object Settings : NavItems("settings_screen", "Settings", Icons.Default.Settings)
    object Info : NavItems("info_screen", "Info", Icons.Default.Info)
    object Profile : NavItems("profile_screen", "Profile", Icons.Default.Person)
    object Login : NavItems("login_screen", "Login", Icons.Default.Settings)
    object Register : NavItems("register_screen", "Register", Icons.Default.Settings)
    object ForgotPassword : NavItems("forgot_password_screen", "Forgot Password", Icons.Default.Close)
}