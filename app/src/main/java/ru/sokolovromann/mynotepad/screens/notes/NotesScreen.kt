package ru.sokolovromann.mynotepad.screens.notes

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
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
import ru.sokolovromann.mynotepad.MyNotepadRoute
import ru.sokolovromann.mynotepad.R
import ru.sokolovromann.mynotepad.screens.notes.components.*
import ru.sokolovromann.mynotepad.ui.components.DefaultFloatingActionButton
import ru.sokolovromann.mynotepad.ui.components.NavigationDrawer
import ru.sokolovromann.mynotepad.ui.components.NavigationDrawerHeader

@ExperimentalFoundationApi
@Composable
fun NotesScreen(
    notesViewModel: NotesViewModel = hiltViewModel(),
    navController: NavController
) {
    val notesState = notesViewModel.notesState
    val noteMenuState = notesViewModel.noteMenuState
    val scaffoldState = rememberScaffoldState()

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
            }
        }
    }

    BackHandler(enabled = scaffoldState.drawerState.isOpen) {
        notesViewModel.onEvent(NotesEvent.NavigationMenuStateChange(false))
    }

    Scaffold(
        topBar = {
            NotesTopAppBar(onNavigationIconClick = {
                notesViewModel.onEvent(NotesEvent.NavigationMenuStateChange(true))
            })
        },
        floatingActionButton = {
            DefaultFloatingActionButton(onClick = {
                notesViewModel.onEvent(NotesEvent.AddNoteClick)
            })
        },
        scaffoldState = scaffoldState,
        snackbarHost = { scaffoldState.snackbarHostState },
        drawerContent = {
            NavigationDrawer(
                navController = navController,
                closeNavigation = {
                    notesViewModel.onEvent(NotesEvent.NavigationMenuStateChange(false)
                )},
                drawerHeader = {
                    NavigationDrawerHeader(title = stringResource(id = R.string.app_name))
                }
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
                snackbarHostState = scaffoldState.snackbarHostState
            )

            NotesState.NotFound -> NotesNotFound()
        }
    }
}