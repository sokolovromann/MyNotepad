package ru.sokolovromann.mynotepad.screens.addeditnote

import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.sokolovromann.mynotepad.R
import ru.sokolovromann.mynotepad.screens.addeditnote.components.AddEditNoteDisplay
import ru.sokolovromann.mynotepad.screens.addeditnote.components.AddEditNoteTopAppBar

@Composable
fun AddEditNoteScreen(
    addEditNoteViewModel: AddEditNoteViewModel = hiltViewModel(),
    navController: NavController
) {
    val addEditNoteState = addEditNoteViewModel.addEditNoteState
    val showKeyboardState = addEditNoteViewModel.showKeyboardState
    val scaffoldState = rememberScaffoldState()

    val coroutineScope = rememberCoroutineScope()

    val savedMessage = stringResource(id = R.string.add_edit_note_saved_message)

    LaunchedEffect(true) {
        addEditNoteViewModel.addEditNoteUiEvent.collectLatest { uiEvent ->
            when (uiEvent) {
                AddEditNoteUiEvent.OpenNotes -> navController.popBackStack()
                AddEditNoteUiEvent.ShowSavedMessage -> coroutineScope.launch {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = savedMessage
                    )
                }
            }
        }
    }

    Scaffold(
        topBar = {
            AddEditNoteTopAppBar(
                onBackClick = { addEditNoteViewModel.onEvent(AddEditNoteEvent.BackClick) },
                onSaveClick = { addEditNoteViewModel.onEvent(AddEditNoteEvent.SaveNoteClick) }
            )
        },
        scaffoldState = scaffoldState,
        snackbarHost = { scaffoldState.snackbarHostState }
    ) {
        AddEditNoteDisplay(
            title = addEditNoteState.value.title,
            text = addEditNoteState.value.text,
            onTitleChange = { newTitle -> addEditNoteViewModel.onEvent(AddEditNoteEvent.OnTitleChange(newTitle)) },
            onTextChange = { newText -> addEditNoteViewModel.onEvent(AddEditNoteEvent.OnTextChange(newText)) },
            snackbarHostState = scaffoldState.snackbarHostState,
            emptyTextError = addEditNoteState.value.emptyTextError,
            showKeyboard = showKeyboardState.value
        )
    }
}