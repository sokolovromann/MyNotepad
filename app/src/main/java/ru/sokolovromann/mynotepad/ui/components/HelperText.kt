package ru.sokolovromann.mynotepad.ui.components

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun DefaultHelperText(
    modifier: Modifier = Modifier,
    helperText: String,
    errorText: String = "",
    helperColor: Color = Color.Unspecified,
    isError: Boolean = false
) {
    Text(
        text = if (isError) errorText else helperText,
        style = MaterialTheme.typography.caption,
        color = if (isError) MaterialTheme.colors.error else helperColor,
        modifier = modifier
    )
}