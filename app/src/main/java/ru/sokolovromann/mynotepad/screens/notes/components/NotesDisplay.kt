package ru.sokolovromann.mynotepad.screens.notes.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.MaterialTheme
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.sokolovromann.mynotepad.R
import ru.sokolovromann.mynotepad.data.local.note.Note
import ru.sokolovromann.mynotepad.data.local.settings.NotesSort
import ru.sokolovromann.mynotepad.ui.components.*
import ru.sokolovromann.mynotepad.ui.theme.MyNotepadTheme

@ExperimentalFoundationApi
@Composable
fun NotesDisplay(
    notes: List<Note>,
    onNoteClick: (note: Note) -> Unit,
    noteMenuIndex: Int,
    onNoteMenuIndexChange: (newIndex: Int) -> Unit,
    onDeleteNote: (note: Note) -> Unit,
    onNoteDeletedUndo: () -> Unit,
    snackbarHostState: SnackbarHostState,
    onSortClick: () -> Unit,
    notesSort: NotesSort
) {
    Box {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            itemsIndexed(notes) { index, note ->
                if (index == 0) {
                    DefaultTextButton(
                        onClick = onSortClick,
                        text = when (notesSort) {
                            NotesSort.CREATED_ASC -> stringResource(id = R.string.notes_sort_created_asc)
                            NotesSort.CREATED_DESC -> stringResource(id = R.string.notes_sort_created_desc)
                            NotesSort.LAST_MODIFIED_ASC -> stringResource(id = R.string.notes_sort_last_modified_asc)
                            NotesSort.LAST_MODIFIED_DESC -> stringResource(id = R.string.notes_sort_last_modified_desc)
                        },
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )
                }
                ClickableSurface(
                    onClick = { onNoteClick(note) },
                    onLongClick = { onNoteMenuIndexChange(index) },
                    modifier = Modifier.padding(horizontal = 8.dp),
                    shape = MaterialTheme.shapes.medium,
                    elevation = 1.dp
                ) {
                    TextItem(
                        modifier = Modifier.padding(8.dp),
                        title = if (note.title.isEmpty()) {
                            null
                        } else {
                            { Title(text = note.title) }
                        },
                        body = { Body(text = note.text)},
                        dropdownMenu = {
                            NotesDropdownMenu(
                                expanded = noteMenuIndex == index,
                                onDismiss = { onNoteMenuIndexChange(-1) },
                                onDeleteClick = { onDeleteNote(note) }
                            )
                        }
                    )
                }
                TransparentDivider(thickness = if (index == notes.size - 1) 128.dp else 8.dp)
            }
        }

        DefaultSnackbar(
            snackbarHostState = snackbarHostState,
            onActionClick = onNoteDeletedUndo,
            modifier = Modifier
                .padding(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 88.dp)
                .align(alignment = Alignment.BottomCenter)
        )
    }
}

@Composable
private fun Title(text: String) {
    ItemTitleText(
        text = text,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 4.dp),
    )
}

@Composable
private fun Body(text: String) {
    ItemBodyText(
        text = text,
        modifier = Modifier.fillMaxWidth()
    )
}

@ExperimentalFoundationApi
@Preview(showBackground = true)
@Composable
private fun NoteDisplayPreview() {
    MyNotepadTheme {
        val notes = listOf(
            Note(title = "Title ".repeat(3), text = "Text ".repeat(20)),
            Note(title = "", text = "Text ".repeat(10)),
            Note(title = "Title ".repeat(4), text = "Text ".repeat(10)),
            Note(title = "Title", text = "Text ".repeat(15)),
            Note(title = "", text = "Text ".repeat(50))
        )
        NotesDisplay(
            notes = notes,
            onNoteClick = {},
            noteMenuIndex = -1,
            onNoteMenuIndexChange = {},
            onDeleteNote = {},
            onNoteDeletedUndo = {},
            snackbarHostState = rememberScaffoldState().snackbarHostState,
            onSortClick = {},
            notesSort = NotesSort.CREATED_DESC
        )
    }
}