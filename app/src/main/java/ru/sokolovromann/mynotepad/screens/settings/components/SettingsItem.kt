package ru.sokolovromann.mynotepad.screens.settings.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ru.sokolovromann.mynotepad.ui.components.*

@Composable
fun SettingsItem(
    title: String,
    description: String = "",
    icon: Painter? = null,
    onItemClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .height(if (description.isEmpty()) 56.dp else 64.dp)
            .clickable { onItemClick() }
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        icon?.let {
            ItemIcon(painter = icon)
        }
        TextItem(
            modifier = Modifier
                .padding(start = if (icon == null) 56.dp else 32.dp, end = 16.dp)
                .weight(1f),
            title = { ItemTitleText(
                text = title,
                maxLines = 1,
                fontWeight = FontWeight.Normal)
            },
            body = if (description.isEmpty()) null else {
                { ItemBodyText(
                    text = description,
                    maxLines = 1,
                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f),
                    style = MaterialTheme.typography.body2
                ) }
            }
        )
    }
}