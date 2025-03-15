package com.pwojtowicz.buybuddies.ui.theme


import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.pwojtowicz.buybuddies.BuyBuddiesApplication
import com.pwojtowicz.buybuddies.ui.message.AppMessageHandler

private val DarkColorPalette = darkColorScheme(
    primary = bb_theme_main_color,
    onPrimary = bb_theme_text_clr_light,
    primaryContainer = bb_theme_main_selected_clr,
    onPrimaryContainer = bb_theme_text_clr_light,
    onSecondary = Color.Black,
    surface = bb_theme_main_color,
    onSurface = bb_theme_text_clr_light,
    background = bb_theme_main_color,
    onBackground = Color.Black,
    surfaceVariant = bb_theme_container_clr_light
)


private val LightColorPalette = lightColorScheme(
    primary = bb_theme_main_color,
    onPrimary = bb_theme_text_clr_light,
    primaryContainer = bb_theme_main_selected_clr,
    onPrimaryContainer = bb_theme_text_clr_light,
    onSecondary = Color.Black,
    surface = bb_theme_main_color,
    onSurface = bb_theme_text_clr_light,
    background = bb_theme_main_color,
    onBackground = Color.Black,
    surfaceVariant = bb_theme_container_clr_light,
)

@Composable
fun BuyBuddiesTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorPalette else LightColorPalette
    val systemUiController = rememberSystemUiController()

    SideEffect {
        systemUiController.setStatusBarColor(
            color = if (darkTheme) bb_theme_dark_system_color else bb_theme_main_color,
            darkIcons = !darkTheme
        )
        systemUiController.setNavigationBarColor(
            color = if (darkTheme) bb_theme_black_clr else Color.White,
            darkIcons = !darkTheme
        )
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}