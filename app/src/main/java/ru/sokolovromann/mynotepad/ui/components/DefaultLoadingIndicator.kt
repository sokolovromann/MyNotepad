package ru.sokolovromann.mynotepad.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.sokolovromann.mynotepad.ui.theme.MyNotepadTheme

@Composable
fun DefaultLoadingIndicator(
    modifier: Modifier = Modifier,
    message: String = "",
    messageColor: Color = MaterialTheme.colors.onSurface,
    indicatorColor: Color = MaterialTheme.colors.primary
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.padding(all = 8.dp),
            color = indicatorColor
        )
        Text(
            style = MaterialTheme.typography.body1,
            color = messageColor,
            text = message
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DefaultLoadingIndicatorPreview() {
    MyNotepadTheme {
        DefaultLoadingIndicator(message = "Loading... Please wait...")
    }
}