package ru.sokolovromann.mynotepad.screens.notes

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.navigation.NavController
import ru.sokolovromann.mynotepad.screens.MainRoute
import ru.sokolovromann.mynotepad.screens.notes.components.NotesDisplay
import ru.sokolovromann.mynotepad.screens.notes.components.NotesLoading
import ru.sokolovromann.mynotepad.screens.notes.components.NotesNotFound
import ru.sokolovromann.mynotepad.screens.notes.components.NotesTopAppBar
import ru.sokolovromann.mynotepad.ui.components.DefaultFloatingActionButton

@ExperimentalMaterialApi
@Composable
fun NotesScreen(
    notesViewModel: NotesViewModel,
    navController: NavController
) {
    notesViewModel.getNotes()
    val notesState = notesViewModel.notesState.observeAsState()

    Scaffold(
        topBar = { NotesTopAppBar() },
        floatingActionButton = {
            DefaultFloatingActionButton(onClick = {
                navController.navigate(MainRoute.AddNote.route)
            })
        }
    ) {
        when (val state = notesState.value) {
            NotesState.Loading -> {
                NotesLoading()
            }

            is NotesState.Notes -> {
                NotesDisplay(
                    notes = state.notes,
                    onNoteClick = { note -> navController.navigate(MainRoute.EditNote(note.uid).route) }
                )
            }

            NotesState.NotFound -> {
                NotesNotFound()
            }
        }
    }
}