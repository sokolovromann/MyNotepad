package ru.sokolovromann.mynotepad.screens.notes

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import ru.sokolovromann.mynotepad.MyNotepadRoute
import ru.sokolovromann.mynotepad.R
import ru.sokolovromann.mynotepad.data.local.note.Note
import ru.sokolovromann.mynotepad.screens.addeditnote.DELETED_NOTE_JSON
import ru.sokolovromann.mynotepad.screens.addeditnote.NOTE_SAVED
import ru.sokolovromann.mynotepad.screens.notes.components.*
import ru.sokolovromann.mynotepad.ui.components.*

@ExperimentalMaterialApi
@ExperimentalFoundationApi
@Composable
fun NotesScreen(
    viewModel: NotesViewModel = hiltViewModel(),
    navController: NavController
) {
    val notesItemsState = viewModel.notesItemsState
    val accountState = viewModel.accountState
    val notesSortState = viewModel.notesSortState
    val notesMultiColumnsState = viewModel.notesMultiColumnsState
    val scaffoldState = rememberScaffoldState()
    val sheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
    val scrollState = rememberScrollState()

    val coroutineScope = rememberCoroutineScope()

    val deletedMessage = stringResource(id = R.string.notes_note_deleted_message)
    val savedMessage = stringResource(id = R.string.notes_note_saved_message)
    val noteDeletedUndo = stringResource(id = R.string.notes_note_deleted_undo)

    val navArguments = navController.currentBackStackEntry?.arguments
    LaunchedEffect(navArguments) {
        val deletedNote = navArguments?.getString(DELETED_NOTE_JSON)?.let {
            Json.decodeFromString<Note>(it)
        } ?: Note.EMPTY
        viewModel.onEvent(NotesEvent.NoteDeleted(deletedNote = deletedNote))

        val noteSaved = navArguments?.getBoolean(NOTE_SAVED) ?: false
        viewModel.onEvent(NotesEvent.NoteSaved(noteSaved = noteSaved))
    }

    LaunchedEffect(Unit) {
        viewModel.notesUiEvent.collectLatest { uiEvent ->
            when (uiEvent) {
                NotesUiEvent.AddNote -> navController.navigate(MyNotepadRoute.Notes.addNoteScreen)

                is NotesUiEvent.EditNote -> navController.navigate(MyNotepadRoute.Notes.editNoteScreen(uiEvent.note.uid))

                NotesUiEvent.ShowDeletedMessage -> coroutineScope.launch {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = deletedMessage,
                        actionLabel = noteDeletedUndo
                    )
                }

                NotesUiEvent.ShowSavedMessage -> coroutineScope.launch {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = savedMessage
                    )
                }

                NotesUiEvent.OpenNavigationMenu -> coroutineScope.launch {
                    scaffoldState.drawerState.open()
                }

                NotesUiEvent.CloseNavigationMenu -> coroutineScope.launch {
                    scaffoldState.drawerState.close()
                }

                NotesUiEvent.OpenSortNotesSheet -> coroutineScope.launch {
                    sheetState.show()
                }

                NotesUiEvent.CloseSortNotesSheet -> coroutineScope.launch {
                    sheetState.hide()
                }
            }
        }
    }

    BackHandler(enabled = scaffoldState.drawerState.isOpen) {
        viewModel.onEvent(NotesEvent.OnNavigationMenuStateChange(false))
    }

    BackHandler(enabled = sheetState.isVisible) {
        viewModel.onEvent(NotesEvent.OnNotesSortSheetStateChange(false))
    }

    ModalBottomSheetLayout(
        sheetContent = {
            NotesSortSheet(
                notesSort = notesSortState.value,
                onNotesSortChange = { notesSort ->
                    viewModel.onEvent(NotesEvent.OnNotesSortChange(notesSort))
                    viewModel.onEvent(NotesEvent.OnNotesSortSheetStateChange(false))
                }
            )
        },
        sheetState = sheetState
    ) {
        Scaffold(
            topBar = {
                NotesTopAppBar(
                    onNavigationIconClick = { viewModel.onEvent(NotesEvent.OnNavigationMenuStateChange(true)) },
                )
            },
            floatingActionButton = {
                IconFloatingActionButton(onClick = {
                    viewModel.onEvent(NotesEvent.AddNoteClick)
                })
            },
            scaffoldState = scaffoldState,
            snackbarHost = { scaffoldState.snackbarHostState },
            drawerContent = {
                NavigationDrawer(
                    navController = navController,
                    closeNavigation = {
                        viewModel.onEvent(NotesEvent.OnNavigationMenuStateChange(false))},
                    drawerHeader = {
                        NavigationDrawerHeader(
                            title = stringResource(id = R.string.app_name),
                            description = accountState.value.getName()
                        )
                    },
                    onRefresh = { viewModel.onEvent(NotesEvent.RefreshNotesClick) }
                )
            }
        ) {
            if (viewModel.loadingState.value) {
                NotesLoading()
                return@Scaffold
            }

            if (notesItemsState.value.isEmpty()) {
                NotesNotFound()
                return@Scaffold
            }

            Box {
                Column(modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                ) {
                    Row {
                        NotesSortButton(notesSort = notesSortState.value) {
                            viewModel.onEvent(NotesEvent.OnNotesSortSheetStateChange(true))
                        }
                        Spacer(modifier = Modifier.weight(1f))
                        NotesMultiColumnsButton(multiColumns = notesMultiColumnsState.value) {
                            viewModel.onEvent(NotesEvent.NotesMultiColumnsClick)
                        }
                    }
                    StaggeredVerticalGrid(
                        modifier = Modifier.padding(horizontal = 4.dp),
                        multiColumns = notesMultiColumnsState.value
                    ) {
                        notesItemsState.value.forEachIndexed { index, itemState ->
                            NotesItem(
                                itemState = itemState,
                                onClick = { viewModel.onEvent(NotesEvent.NoteClick(itemState.note)) },
                                onShowMenuChange = { viewModel.onEvent(NotesEvent.OnNoteMenuChange(it, itemState.note)) },
                                onDelete = { viewModel.onEvent(NotesEvent.DeleteNoteClick(itemState.note)) }
                            )
                        }
                    }
                    TransparentDivider(thickness = 128.dp)
                }
                DefaultSnackbar(
                    snackbarHostState = scaffoldState.snackbarHostState,
                    onActionClick = { viewModel.onEvent(NotesEvent.NoteDeletedUndoClick) },
                    modifier = Modifier
                        .padding(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 88.dp)
                        .align(alignment = Alignment.BottomCenter)
                )
            }
        }
    }
}