package ru.sokolovromann.mynotepad.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.sokolovromann.mynotepad.ui.theme.MyNotepadTheme

@Composable
fun DefaultNotFoundMessage(
    modifier: Modifier = Modifier,
    imageVector: ImageVector,
    message: String,
    messageColor: Color = MaterialTheme.colors.onSurface
) {
    NotFoundMessageColumn(modifier = modifier) {
        Icon(
            modifier = Modifier.padding(8.dp),
            imageVector = imageVector,
            contentDescription = ""
        )
        NotFoundMessageText(
            message = message,
            messageColor = messageColor
        )
    }
}

@Composable
fun DefaultNotFoundMessage(
    modifier: Modifier = Modifier,
    painter: Painter,
    message: String,
    messageColor: Color = MaterialTheme.colors.onSurface
) {
    NotFoundMessageColumn(modifier = modifier) {
        Icon(
            modifier = Modifier.padding(8.dp),
            painter = painter,
            contentDescription = ""
        )
        NotFoundMessageText(
            message = message,
            messageColor = messageColor
        )
    }
}

@Composable
fun DefaultNotFoundMessage(
    modifier: Modifier = Modifier,
    message: String,
    messageColor: Color = MaterialTheme.colors.onSurface
) {
    NotFoundMessageColumn(modifier = modifier) {
        NotFoundMessageText(
            message = message,
            messageColor = messageColor
        )
    }
}

@Composable
private fun NotFoundMessageColumn(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        content = content
    )
}

@Composable
private fun NotFoundMessageText(
    message: String,
    messageColor: Color
) {
    Text(
        style = MaterialTheme.typography.body1,
        color = messageColor,
        text = message,
    )
}

@Preview(showBackground = true)
@Composable
private fun DefaultNotFoundMessagePreview() {
    MyNotepadTheme {
        DefaultNotFoundMessage(message = "Data is not found")
    }
}