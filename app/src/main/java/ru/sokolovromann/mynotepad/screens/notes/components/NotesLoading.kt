package ru.sokolovromann.mynotepad.screens.notes.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import ru.sokolovromann.mynotepad.ui.components.DefaultLoadingIndicator
import ru.sokolovromann.mynotepad.ui.theme.MyNotepadTheme

@Composable
fun NotesLoading() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        DefaultLoadingIndicator()
    }
}

@Preview(showBackground = true)
@Composable
fun NotesLoadingPreview() {
    MyNotepadTheme {
        NotesLoading()
    }
}