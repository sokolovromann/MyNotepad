package ru.sokolovromann.mynotepad.screens.settings.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.sokolovromann.mynotepad.ui.components.ItemTitleText

@Composable
fun SettingsHeader(
    text: String
) {
    ItemTitleText(
        text = text,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 72.dp, top = 16.dp, end = 16.dp, bottom = 8.dp),
        color = MaterialTheme.colors.primary
    )
}