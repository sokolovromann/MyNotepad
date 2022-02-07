package ru.sokolovromann.mynotepad.screens.notes.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import ru.sokolovromann.mynotepad.ui.components.CircularLoading
import ru.sokolovromann.mynotepad.ui.theme.MyNotepadTheme

@Composable
fun NotesLoading() {
    CircularLoading(
        modifier = Modifier.fillMaxSize()
    )
}

@Preview(showBackground = true)
@Composable
private fun NotesLoadingPreview() {
    MyNotepadTheme {
        NotesLoading()
    }
}