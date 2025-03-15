package com.pwojtowicz.buybuddies.navigation

sealed class NavRoute(val route: String) {
    object Auth : NavRoute("auth")
    object Main : NavRoute("main")
}