package ru.sokolovromann.mynotepad.screens.notes

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import ru.sokolovromann.mynotepad.R
import ru.sokolovromann.mynotepad.screens.MainRoute
import ru.sokolovromann.mynotepad.screens.notes.components.*
import ru.sokolovromann.mynotepad.ui.components.DefaultFloatingActionButton

@ExperimentalFoundationApi
@Composable
fun NotesScreen(
    notesViewModel: NotesViewModel,
    navController: NavController
) {
    notesViewModel.getNotes()
    val notesState = notesViewModel.notesState.observeAsState()

    val scaffoldState = rememberScaffoldState()

    val coroutineScope = rememberCoroutineScope()

    val deletedMessage = stringResource(id = R.string.notes_note_deleted_message)

    val noteDeletedUndo = stringResource(id = R.string.notes_note_deleted_undo)

    Scaffold(
        topBar = { NotesTopAppBar() },
        floatingActionButton = {
            DefaultFloatingActionButton(onClick = {
                navController.navigate(MainRoute.AddNote.route)
            })
        },
        scaffoldState = scaffoldState,
        snackbarHost = { scaffoldState.snackbarHostState }
    ) {
        when (val state = notesState.value) {
            NotesState.Loading -> {
                NotesLoading()
            }

            is NotesState.Notes -> {
                NotesDisplay(
                    notes = state.notes,
                    onNoteClick = { note -> navController.navigate(MainRoute.EditNote(note.uid).route) },
                    noteMenuPosition = state.noteMenuPosition.value,
                    onNoteMenuPositionChange = { newPosition -> state.onNoteMenuPositionChange(newPosition) },
                    onDeleteNote = { note ->
                        notesViewModel.deleteNote(note)
                        coroutineScope.launch {
                            scaffoldState.snackbarHostState.showSnackbar(
                                message = deletedMessage,
                                actionLabel = noteDeletedUndo
                            )
                        }
                    },
                    onNoteDeletedUndo = { state.onRestoreLastNote() },
                    snackbarHostState = scaffoldState.snackbarHostState
                )
            }

            NotesState.NotFound -> {
                NotesNotFound()
            }
        }
    }
}