package ru.sokolovromann.mynotepad.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import com.google.accompanist.systemuicontroller.rememberSystemUiController

private val DarkColorPalette = darkColors(
    primary = Blue500,
    primaryVariant = DarkBlue900,
    onPrimary = White,

    secondary = Blue500,
    secondaryVariant = DarkBlue900,
    onSecondary = White,

    background = DarkGray900,
    onBackground = White,

    surface = Gray900,
    onSurface = White,

    error = Red700,
    onError = White
)

private val LightColorPalette = lightColors(
    primary = Blue900,
    primaryVariant = DarkBlue900,
    onPrimary = White,

    secondary = Blue900,
    secondaryVariant = DarkBlue900,
    onSecondary = White,

    background = Gray200,
    onBackground = Black,

    surface = White,
    onSurface = Black,

    error = Red700,
    onError = White
)

@Composable
fun MyNotepadTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable() () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )

    val systemUiController = rememberSystemUiController()
    systemUiController.setStatusBarColor(
        color = if (darkTheme) {
            DarkColorPalette.surface
        } else {
            LightColorPalette.primaryVariant
        },
        darkIcons = false
    )
    systemUiController.setNavigationBarColor(
        color = if (darkTheme) {
            DarkColorPalette.surface
        } else {
            LightColorPalette.surface
        },
        darkIcons = !darkTheme
    )
}