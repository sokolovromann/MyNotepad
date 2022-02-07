package ru.sokolovromann.mynotepad.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.sokolovromann.mynotepad.ui.theme.MyNotepadTheme

@Composable
fun IconFloatingActionButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    shape: Shape = MaterialTheme.shapes.small.copy(CornerSize(percent = 50)),
    backgroundColor: Color = MaterialTheme.colors.secondary,
    contentColor: Color = MaterialTheme.colors.onSecondary,
    elevation: FloatingActionButtonElevation = FloatingActionButtonDefaults.elevation(),
    icon: ImageVector = Icons.Filled.Add,
    contentDescription: String = ""
) {
    FloatingActionButton(
        onClick = onClick,
        modifier = modifier,
        interactionSource = interactionSource,
        shape = shape,
        backgroundColor = backgroundColor,
        contentColor = contentColor,
        elevation = elevation
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription
        )
    }
}

@Composable
fun DefaultTextButton(
    modifier: Modifier = Modifier,
    textColor: Color = MaterialTheme.colors.primary,
    onClick: () -> Unit,
    text: String,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    elevation: ButtonElevation? = null,
    shape: Shape = MaterialTheme.shapes.small,
    border: BorderStroke? = null,
    contentPadding: PaddingValues = ButtonDefaults.TextButtonContentPadding,
) {
    TextButton(
        modifier = modifier,
        colors = ButtonDefaults.textButtonColors(contentColor = textColor),
        onClick = onClick,
        enabled = enabled,
        interactionSource = interactionSource,
        elevation = elevation,
        shape = shape,
        border = border,
        contentPadding = contentPadding
    ) {
        Text(text = text)
    }
}

@Preview(showBackground = true)
@Composable
private fun ButtonPreview() {
    MyNotepadTheme {
        IconFloatingActionButton(onClick = {})
        Spacer(modifier = Modifier.height(8.dp))
        DefaultTextButton(onClick = {}, text = "Text Button")
    }
}