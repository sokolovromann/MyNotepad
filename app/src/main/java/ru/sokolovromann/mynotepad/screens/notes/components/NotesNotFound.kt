package ru.sokolovromann.mynotepad.screens.notes.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import ru.sokolovromann.mynotepad.R
import ru.sokolovromann.mynotepad.ui.components.NotFound
import ru.sokolovromann.mynotepad.ui.components.NotFoundIcon
import ru.sokolovromann.mynotepad.ui.theme.MyNotepadTheme

@Composable
fun NotesNotFound() {
    NotFound(
        modifier = Modifier.fillMaxSize(),
        message = stringResource(id = R.string.notes_not_found_message),
        icon = {
            NotFoundIcon(icon = painterResource(id = R.drawable.ic_notes_not_found))
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun NotesNotFoundPreview() {
    MyNotepadTheme {
        NotesNotFound()
    }
}