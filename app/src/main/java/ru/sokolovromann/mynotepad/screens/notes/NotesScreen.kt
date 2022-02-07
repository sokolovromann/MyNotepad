package ru.sokolovromann.mynotepad.screens.notes

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.sokolovromann.mynotepad.MyNotepadRoute
import ru.sokolovromann.mynotepad.R
import ru.sokolovromann.mynotepad.screens.notes.components.*
import ru.sokolovromann.mynotepad.ui.components.IconFloatingActionButton
import ru.sokolovromann.mynotepad.ui.components.NavigationDrawer
import ru.sokolovromann.mynotepad.ui.components.NavigationDrawerHeader

@ExperimentalMaterialApi
@ExperimentalFoundationApi
@Composable
fun NotesScreen(
    notesViewModel: NotesViewModel = hiltViewModel(),
    navController: NavController
) {
    val notesState = notesViewModel.notesState
    val noteMenuState = notesViewModel.noteMenuState
    val accountState = notesViewModel.accountState
    val notesSortState = notesViewModel.notesSortState
    val notesSyncState = notesViewModel.notesSyncState
    val scaffoldState = rememberScaffoldState()
    val sheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)

    val coroutineScope = rememberCoroutineScope()

    val deletedMessage = stringResource(id = R.string.notes_note_deleted_message)
    val noteDeletedUndo = stringResource(id = R.string.notes_note_deleted_undo)

    LaunchedEffect(true) {
        notesViewModel.notesUiEvent.collectLatest { uiEvent ->
            when (uiEvent) {
                NotesUiEvent.AddNote -> navController.navigate(MyNotepadRoute.Notes.addNoteScreen)

                is NotesUiEvent.EditNote -> navController.navigate(MyNotepadRoute.Notes.editNoteScreen(uiEvent.note.uid))

                NotesUiEvent.ShowDeletedMessage -> coroutineScope.launch {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = deletedMessage,
                        actionLabel = noteDeletedUndo
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
        notesViewModel.onEvent(NotesEvent.OnNavigationMenuStateChange(false))
    }

    BackHandler(enabled = sheetState.isVisible) {
        notesViewModel.onEvent(NotesEvent.OnNotesSortSheetStateChange(false))
    }

    ModalBottomSheetLayout(
        sheetContent = {
            NotesSortSheet(
                notesSort = notesSortState.value,
                onNotesSortChange = { notesSort ->
                    notesViewModel.onEvent(NotesEvent.OnNotesSortChange(notesSort))
                    notesViewModel.onEvent(NotesEvent.OnNotesSortSheetStateChange(false))
                }
            )
        },
        sheetState = sheetState
    ) {
        Scaffold(
            topBar = {
                NotesTopAppBar(
                    onNavigationIconClick = { notesViewModel.onEvent(NotesEvent.OnNavigationMenuStateChange(true)) },
                    syncing = notesSyncState.value.syncing
                )
            },
            floatingActionButton = {
                IconFloatingActionButton(onClick = {
                    notesViewModel.onEvent(NotesEvent.AddNoteClick)
                })
            },
            scaffoldState = scaffoldState,
            snackbarHost = { scaffoldState.snackbarHostState },
            drawerContent = {
                NavigationDrawer(
                    navController = navController,
                    closeNavigation = {
                        notesViewModel.onEvent(NotesEvent.OnNavigationMenuStateChange(false))},
                    drawerHeader = {
                        NavigationDrawerHeader(
                            title = stringResource(id = R.string.app_name),
                            description = accountState.value.getName()
                        )
                    },
                    onRefresh = { notesViewModel.onEvent(NotesEvent.RefreshNotesClick) }
                )
            }
        ) {
            when (val state = notesState.value) {
                NotesState.Loading -> NotesLoading()

                is NotesState.Notes -> NotesDisplay(
                    notes = state.notes,
                    onNoteClick = { note -> notesViewModel.onEvent(NotesEvent.NoteClick(note)) },
                    noteMenuIndex = noteMenuState.value,
                    onNoteMenuIndexChange = { newIndex -> notesViewModel.onEvent(NotesEvent.OpenNoteMenu(newIndex))},
                    onDeleteNote = { note -> notesViewModel.onEvent(NotesEvent.DeleteNoteClick(note)) },
                    onNoteDeletedUndo = { notesViewModel.onEvent(NotesEvent.NoteDeletedUndoClick) },
                    snackbarHostState = scaffoldState.snackbarHostState,
                    onSortClick = { notesViewModel.onEvent(NotesEvent.OnNotesSortSheetStateChange(true)) },
                    notesSort = notesSortState.value
                )

                NotesState.NotFound -> NotesNotFound()
            }
        }
    }
}