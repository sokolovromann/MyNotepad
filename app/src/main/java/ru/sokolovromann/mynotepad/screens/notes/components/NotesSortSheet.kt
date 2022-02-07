package ru.sokolovromann.mynotepad.screens.notes.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.sokolovromann.mynotepad.R
import ru.sokolovromann.mynotepad.data.local.settings.NotesSort
import ru.sokolovromann.mynotepad.ui.components.RadioButtonItem
import ru.sokolovromann.mynotepad.ui.theme.MyNotepadTheme

@Composable
fun NotesSortSheet(
    notesSort: NotesSort,
    onNotesSortChange: (notesSort: NotesSort) -> Unit
) {
    Column {
        Text(
            modifier = Modifier.padding(all = 16.dp),
            text = stringResource(id = R.string.notes_sort_by),
            style = MaterialTheme.typography.body1
        )
        RadioButtonItem(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            title = stringResource(id = R.string.notes_sort_created_asc),
            selected = notesSort == NotesSort.CREATED_ASC,
            onSelectedChange = { onNotesSortChange(NotesSort.CREATED_ASC) }
        )
        RadioButtonItem(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            title = stringResource(id = R.string.notes_sort_created_desc),
            selected = notesSort == NotesSort.CREATED_DESC,
            onSelectedChange = { onNotesSortChange(NotesSort.CREATED_DESC) }
        )
        RadioButtonItem(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            title = stringResource(id = R.string.notes_sort_last_modified_asc),
            selected = notesSort == NotesSort.LAST_MODIFIED_ASC,
            onSelectedChange = { onNotesSortChange(NotesSort.LAST_MODIFIED_ASC) }
        )
        RadioButtonItem(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            title = stringResource(id = R.string.notes_sort_last_modified_desc),
            selected = notesSort == NotesSort.LAST_MODIFIED_DESC,
            onSelectedChange = { onNotesSortChange(NotesSort.LAST_MODIFIED_DESC) }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun NotesSortSheetPreview() {
    MyNotepadTheme {
        NotesSortSheet(
            notesSort = NotesSort.LAST_MODIFIED_ASC,
            onNotesSortChange = {}
        )
    }
}