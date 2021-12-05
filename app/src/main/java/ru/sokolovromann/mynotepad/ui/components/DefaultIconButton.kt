package ru.sokolovromann.mynotepad.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.sokolovromann.mynotepad.ui.theme.MyNotepadTheme

@Composable
fun DefaultIconButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    imageVector: ImageVector,
    contentDescription: String = "",
) {
    IconButton(
        modifier = modifier,
        onClick = onClick
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = contentDescription
        )
    }
}

@Composable
fun DefaultIconButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    painter: Painter,
    contentDescription: String = "",
) {
    IconButton(
        modifier = modifier,
        onClick = onClick
    ) {
        Icon(
            painter = painter,
            contentDescription = contentDescription
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DefaultIconButtonPreview() {
    MyNotepadTheme {
        Column {
            DefaultIconButton(
                modifier = Modifier.padding(8.dp),
                onClick = { },
                imageVector = Icons.Filled.ArrowBack
            )
            DefaultIconButton(
                modifier = Modifier.padding(8.dp),
                onClick = { },
                imageVector = Icons.Filled.Edit
            )
            DefaultIconButton(
                modifier = Modifier.padding(8.dp),
                onClick = { },
                imageVector = Icons.Filled.Settings
            )
        }
    }
}