package ru.sokolovromann.mynotepad.screens.addeditnote

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ru.sokolovromann.mynotepad.R
import ru.sokolovromann.mynotepad.screens.addeditnote.components.*
import ru.sokolovromann.mynotepad.ui.components.DefaultSnackbar

const val DELETED_NOTE_JSON = "DELETED_NOTE_JSON"
const val NOTE_SAVED = "SAVED_NOTE_JSON"

@Composable
fun AddEditNoteScreen(
    viewModel: AddEditNoteViewModel = hiltViewModel(),
    navController: NavController
) {
    val scaffoldState = rememberScaffoldState()
    val scrollState = rememberScrollState()

    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    val coroutineScope = rememberCoroutineScope()

    val savedMessage = stringResource(id = R.string.add_edit_note_saved_message)

    LaunchedEffect(Unit) {
        viewModel.addEditNoteUiEvent.collectLatest { uiEvent ->
            when (uiEvent) {
                AddEditNoteUiEvent.OpenNotes -> {
                    navController.apply {
                        previousBackStackEntry?.arguments?.clear()
                        popBackStack()
                    }
                }

                AddEditNoteUiEvent.OpenNotesAfterDeleted -> {
                    navController.apply {
                        previousBackStackEntry?.arguments?.apply {
                            clear()
                            putString(DELETED_NOTE_JSON, Json.encodeToString(viewModel.lastDeletedNoteState.value))
                        }
                        popBackStack()
                    }
                }

                AddEditNoteUiEvent.OpenNotesAfterSaved -> {
                    navController.apply {
                        previousBackStackEntry?.arguments?.apply {
                            clear()
                            putBoolean(NOTE_SAVED, true)
                        }
                        popBackStack()
                    }
                }

                AddEditNoteUiEvent.ShowSavedMessage -> coroutineScope.launch {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = savedMessage
                    )
                }

                AddEditNoteUiEvent.ShowKeyboard -> focusRequester.requestFocus()

                AddEditNoteUiEvent.HideKeyboard -> focusManager.clearFocus()
            }
        }
    }

    BackHandler {
        viewModel.onEvent(AddEditNoteEvent.BackClick)
    }

    Scaffold(
        topBar = {
            AddEditNoteTopAppBar(
                onBackClick = { viewModel.onEvent(AddEditNoteEvent.BackClick) },
                onSaveClick = { viewModel.onEvent(AddEditNoteEvent.SaveNoteClick) },
                showMenu = viewModel.addEditNoteMenuState.value,
                onShowMenuChange = { isShow -> viewModel.onEvent(AddEditNoteEvent.OnAddEditNoteMenuChange(isShow)) },
                onDeleteClick = { viewModel.onEvent(AddEditNoteEvent.DeleteNoteClick) }
            )
        },
        scaffoldState = scaffoldState,
        snackbarHost = { scaffoldState.snackbarHostState }
    ) {
        Box(modifier = Modifier.background(MaterialTheme.colors.surface)) {
            Column(modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp)
                .verticalScroll(scrollState)
            ) {
                AddEditNoteTitleField(
                    fieldState = viewModel.noteTitleFieldState.value,
                    onValueChange = { viewModel.onEvent(AddEditNoteEvent.OnTitleChange(it)) }
                )
                AddEditNoteTextField(
                    fieldState = viewModel.noteTextFieldState.value,
                    focusRequester = focusRequester,
                    onValueChange = { viewModel.onEvent(AddEditNoteEvent.OnTextChange(it)) }
                )
            }
            DefaultSnackbar(
                snackbarHostState = scaffoldState.snackbarHostState,
                modifier = Modifier
                    .padding(16.dp)
                    .align(alignment = Alignment.BottomCenter)
            )
        }
    }
}