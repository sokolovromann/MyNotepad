package ru.sokolovromann.mynotepad.ui.components

import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun TransparentDivider(
    modifier: Modifier = Modifier,
    thickness: Dp = 1.dp
) {
    Divider(
        modifier = modifier,
        thickness = thickness,
        color = Color.Transparent
    )
}