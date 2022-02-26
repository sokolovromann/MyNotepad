package ru.sokolovromann.mynotepad.screens.addeditnote

import androidx.activity.compose.BackHandler
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
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ru.sokolovromann.mynotepad.R
import ru.sokolovromann.mynotepad.screens.addeditnote.components.AddEditNoteDisplay
import ru.sokolovromann.mynotepad.screens.addeditnote.components.AddEditNoteTopAppBar

const val DELETED_NOTE_JSON = "DELETED_NOTE_JSON"

@Composable
fun AddEditNoteScreen(
    addEditNoteViewModel: AddEditNoteViewModel = hiltViewModel(),
    navController: NavController
) {
    val addEditNoteState = addEditNoteViewModel.addEditNoteState
    val showKeyboardState = addEditNoteViewModel.showKeyboardState
    val addEditNoteMenuState = addEditNoteViewModel.addEditNoteMenuState
    val lastDeletedNoteState = addEditNoteViewModel.lastDeletedNoteState
    val scaffoldState = rememberScaffoldState()

    val coroutineScope = rememberCoroutineScope()

    val savedMessage = stringResource(id = R.string.add_edit_note_saved_message)

    LaunchedEffect(true) {
        addEditNoteViewModel.addEditNoteUiEvent.collectLatest { uiEvent ->
            when (uiEvent) {
                is AddEditNoteUiEvent.OpenNotes -> {
                    navController.apply {
                        previousBackStackEntry?.arguments
                            ?.putString(DELETED_NOTE_JSON, Json.encodeToString(lastDeletedNoteState.value))
                        popBackStack()
                    }
                }
                AddEditNoteUiEvent.ShowSavedMessage -> coroutineScope.launch {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = savedMessage
                    )
                }
            }
        }
    }

    BackHandler {
        addEditNoteViewModel.onEvent(AddEditNoteEvent.BackClick)
    }

    Scaffold(
        topBar = {
            AddEditNoteTopAppBar(
                onBackClick = { addEditNoteViewModel.onEvent(AddEditNoteEvent.BackClick) },
                onSaveClick = { addEditNoteViewModel.onEvent(AddEditNoteEvent.SaveNoteClick) },
                showMenu = addEditNoteMenuState.value,
                onShowMenuChange = { isShow -> addEditNoteViewModel.onEvent(AddEditNoteEvent.OnAddEditNoteMenuChange(isShow)) },
                onDeleteClick = { addEditNoteViewModel.onEvent(AddEditNoteEvent.DeleteNoteClick) }
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