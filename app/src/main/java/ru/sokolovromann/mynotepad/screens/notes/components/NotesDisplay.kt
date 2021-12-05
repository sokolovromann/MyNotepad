package ru.sokolovromann.mynotepad.screens.notes.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.MaterialTheme
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.sokolovromann.mynotepad.data.local.note.Note
import ru.sokolovromann.mynotepad.ui.components.DefaultCard
import ru.sokolovromann.mynotepad.ui.components.DefaultSnackbar
import ru.sokolovromann.mynotepad.ui.components.TransparentDivider
import ru.sokolovromann.mynotepad.ui.theme.MyNotepadTheme

@ExperimentalFoundationApi
@Composable
fun NotesDisplay(
    notes: List<Note>,
    onNoteClick: (note: Note) -> Unit,
    noteMenuPosition: Int,
    onNoteMenuPositionChange: (newPosition: Int) -> Unit,
    onDeleteNote: (note: Note) -> Unit,
    onNoteDeletedUndo: () -> Unit,
    snackbarHostState: SnackbarHostState
) {
    Box {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            itemsIndexed(notes) { index, note ->
                TransparentDivider(thickness = if (index == 0) 8.dp else 0.dp)
                DefaultCard(
                    onClick = { onNoteClick(note) },
                    onLongClick = { onNoteMenuPositionChange(index) },
                    modifier = Modifier.padding(horizontal = 8.dp)
                ) {
                    Column(modifier = Modifier.padding(8.dp)) {
                        if (note.title.isNotEmpty()) {
                            NotesDisplayTitleText(note.title)
                        }
                        NotesDisplayBodyText(note.text)
                        NotesDropdownMenu(
                            expanded = noteMenuPosition == index,
                            onDismiss = { onNoteMenuPositionChange(-1) },
                            onDeleteClick = { onDeleteNote(note) }
                        )
                    }
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
private fun NotesDisplayTitleText(text: String) {
    Text(
        text = text,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 4.dp),
        fontWeight = FontWeight.Bold,
        overflow = TextOverflow.Ellipsis,
        maxLines = 2,
        style = MaterialTheme.typography.subtitle1
    )
}

@Composable
private fun NotesDisplayBodyText(text: String) {
    Text(
        text = text,
        modifier = Modifier.fillMaxWidth(),
        overflow = TextOverflow.Ellipsis,
        maxLines = 5,
        style = MaterialTheme.typography.body1
    )
}

@ExperimentalFoundationApi
@Preview(showBackground = true)
@Composable
fun NoteDisplayPreview() {
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
            noteMenuPosition = -1,
            onNoteMenuPositionChange = {},
            onDeleteNote = {},
            onNoteDeletedUndo = {},
            snackbarHostState = rememberScaffoldState().snackbarHostState
        )
    }
}