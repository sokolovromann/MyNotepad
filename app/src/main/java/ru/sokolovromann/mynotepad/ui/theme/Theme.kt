package ru.sokolovromann.mynotepad.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

private val DarkColorPalette = darkColors(
    primary = Blue900,
    primaryVariant = DarkBlue900,
    onPrimary = White,

    secondary = Blue900,
    secondaryVariant = DarkBlue900,
    onSecondary = White,

    background = Gray800,
    onBackground = White,

    surface = Black,
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
}