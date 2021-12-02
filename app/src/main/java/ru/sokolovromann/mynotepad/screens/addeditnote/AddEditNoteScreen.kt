package ru.sokolovromann.mynotepad.screens.addeditnote

import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import ru.sokolovromann.mynotepad.R
import ru.sokolovromann.mynotepad.screens.addeditnote.components.AddEditNoteDisplay
import ru.sokolovromann.mynotepad.screens.addeditnote.components.AddEditNoteTopAppBar
import ru.sokolovromann.mynotepad.ui.components.DefaultLoadingIndicator

@Composable
fun AddEditNoteScreen(
    addEditNoteViewModel: AddEditNoteViewModel = hiltViewModel(),
    navController: NavController
) {
    val addEditNoteState = addEditNoteViewModel.addEditNoteState

    val scaffoldState = rememberScaffoldState()

    val coroutineScope = rememberCoroutineScope()

    val savedMessage = stringResource(id = R.string.add_edit_note_saved_message)

    Scaffold(
        topBar = {
            AddEditNoteTopAppBar(
                onBackClick = { navController.popBackStack() },
                onSaveClick = {
                    addEditNoteViewModel.saveNote()
                    coroutineScope.launch {
                        scaffoldState.snackbarHostState.showSnackbar(
                            message = savedMessage
                        )
                    }
                }
            )
        },
        scaffoldState = scaffoldState,
        snackbarHost = { scaffoldState.snackbarHostState }
    ) {
        when (val state = addEditNoteState.value) {
            AddEditNoteState.Loading -> { DefaultLoadingIndicator() }

            is AddEditNoteState.NoteDisplay -> {
                AddEditNoteDisplay(
                    title = state.titleState.value,
                    text = state.textState.value,
                    onTitleChange = { newTitle -> state.onTitleChange(newTitle) },
                    onTextChange = { newText -> state.onTextChange(newText) },
                    snackbarHostState = scaffoldState.snackbarHostState
                )
            }
        }
    }
}