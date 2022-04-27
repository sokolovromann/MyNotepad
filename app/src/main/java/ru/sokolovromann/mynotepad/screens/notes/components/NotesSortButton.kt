package ru.sokolovromann.mynotepad.screens.notes.components

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.sokolovromann.mynotepad.R
import ru.sokolovromann.mynotepad.data.local.settings.NotesSort
import ru.sokolovromann.mynotepad.ui.components.DefaultTextButton

@Composable
fun NotesSortButton(
    notesSort: NotesSort,
    onClick: () -> Unit
) {
    DefaultTextButton(
        onClick = onClick,
        text = when (notesSort) {
            NotesSort.CREATED_ASC -> stringResource(id = R.string.notes_sort_created_asc)
            NotesSort.CREATED_DESC -> stringResource(id = R.string.notes_sort_created_desc)
            NotesSort.LAST_MODIFIED_ASC -> stringResource(id = R.string.notes_sort_last_modified_asc)
            NotesSort.LAST_MODIFIED_DESC -> stringResource(id = R.string.notes_sort_last_modified_desc)
        },
        modifier = Modifier.padding(horizontal = 8.dp)
    )
}