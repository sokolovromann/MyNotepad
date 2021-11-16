package ru.sokolovromann.mynotepad.screens.notes.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import ru.sokolovromann.mynotepad.R
import ru.sokolovromann.mynotepad.ui.components.DefaultNotFoundMessage
import ru.sokolovromann.mynotepad.ui.theme.MyNotepadTheme

@Composable
fun NotesNotFound() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        DefaultNotFoundMessage(
            painter = painterResource(id = R.drawable.ic_notes_not_found),
            message = stringResource(id = R.string.notes_not_found_message)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun NotesNotFoundPreview() {
    MyNotepadTheme {
        NotesNotFound()
    }
}