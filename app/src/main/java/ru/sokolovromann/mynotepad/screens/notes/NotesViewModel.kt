package ru.sokolovromann.mynotepad.screens.notes

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.sokolovromann.mynotepad.data.local.account.Account
import ru.sokolovromann.mynotepad.data.local.note.Note
import ru.sokolovromann.mynotepad.data.local.settings.NotesSort
import ru.sokolovromann.mynotepad.data.repository.AccountRepository
import ru.sokolovromann.mynotepad.data.repository.NoteRepository
import ru.sokolovromann.mynotepad.data.repository.SettingsRepository
import ru.sokolovromann.mynotepad.screens.ScreensEvent
import javax.inject.Inject

@HiltViewModel
class NotesViewModel @Inject constructor(
    private val noteRepository: NoteRepository,
    private val accountRepository: AccountRepository,
    private val settingsRepository: SettingsRepository
) : ViewModel(), ScreensEvent<NotesEvent> {

    private val _notesState: MutableState<NotesState> = mutableStateOf(NotesState.Loading)
    val notesState: State<NotesState> = _notesState

    private val _noteMenuState: MutableState<Int> = mutableStateOf(-1)
    val noteMenuState: State<Int> = _noteMenuState

    private val _accountState: MutableState<Account> = mutableStateOf(Account.LocalAccount)
    val accountState: State<Account> = _accountState

    private val _notesSortState: MutableState<NotesSort> = mutableStateOf(NotesSort.CREATED_ASC)
    val notesSortState: State<NotesSort> = _notesSortState

    private val _notesUiEvent: MutableSharedFlow<NotesUiEvent> = MutableSharedFlow()
    val notesUiEvent: SharedFlow<NotesUiEvent> = _notesUiEvent

    private var lastDeletedNote: Note? = null

    init {
        syncNotes()
        getNotesSort {
            getNotes()
        }
        getAccount()
    }

    override fun onEvent(event: NotesEvent) {
        when (event) {
            NotesEvent.AddNoteClick -> viewModelScope.launch {
                _notesUiEvent.emit(NotesUiEvent.AddNote)
            }

            is NotesEvent.NoteClick -> viewModelScope.launch {
                _notesUiEvent.emit(NotesUiEvent.EditNote(event.note))
            }

            is NotesEvent.OpenNoteMenu -> _noteMenuState.value = event.index

            is NotesEvent.DeleteNoteClick -> deleteNote(event.note)

            NotesEvent.NoteDeletedUndoClick -> restoreLastNote()

            is NotesEvent.OnNavigationMenuStateChange -> viewModelScope.launch {
                if (event.isOpen) {
                    _notesUiEvent.emit(NotesUiEvent.OpenNavigationMenu)
                } else {
                    _notesUiEvent.emit(NotesUiEvent.CloseNavigationMenu)
                }
            }

            is NotesEvent.OnNotesSortSheetStateChange -> viewModelScope.launch {
                if (event.isOpen) {
                    _notesUiEvent.emit(NotesUiEvent.OpenSortNotesSheet)
                } else {
                    _notesUiEvent.emit(NotesUiEvent.CloseSortNotesSheet)
                }
            }

            is NotesEvent.OnNotesSortChange -> saveNotesSort(event.notesSort)
        }
    }

    private fun getNotesSort(onCompleted: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            settingsRepository.getNotesSort().collect { notesSort ->
                withContext(Dispatchers.Main) {
                    _notesSortState.value = notesSort
                    onCompleted()
                }
            }
        }
    }

    private fun saveNotesSort(notesSort: NotesSort) {
        viewModelScope.launch(Dispatchers.IO) {
            settingsRepository.saveNotesSort(notesSort)
        }
    }

    private fun syncNotes() {
        noteRepository.scheduleSyncNotes()
    }

    private fun getNotes() {
        _notesState.value = NotesState.Loading

        viewModelScope.launch(Dispatchers.IO) {
            noteRepository.getNotes().collect { notes ->
                withContext(Dispatchers.Main) {
                    if (notes.isEmpty()) {
                        _notesState.value = NotesState.NotFound
                    } else {
                        _notesState.value = NotesState.Notes(sortNotes(notes))
                    }
                }
            }
        }
    }

    private fun sortNotes(notes: List<Note>): List<Note> {
        return when (_notesSortState.value) {
            NotesSort.CREATED_ASC -> notes.sortedBy { it.created }
            NotesSort.CREATED_DESC -> notes.sortedByDescending { it.created }
            NotesSort.LAST_MODIFIED_ASC -> notes.sortedBy { it.lastModified }
            NotesSort.LAST_MODIFIED_DESC -> notes.sortedByDescending { it.lastModified }
        }
    }

    private fun deleteNote(note: Note) {
        getTokenId { tokenId ->
            viewModelScope.launch(Dispatchers.IO) {
                noteRepository.deleteNote(note, tokenId)

                withContext(Dispatchers.Main) {
                    lastDeletedNote = note
                    _noteMenuState.value = -1
                    _notesUiEvent.emit(NotesUiEvent.ShowDeletedMessage)
                }
            }
        }
    }

    private fun restoreLastNote() {
        getTokenId { tokenId ->
            viewModelScope.launch(Dispatchers.IO) {
                lastDeletedNote?.let {
                    val note = it.copy(id = 0L)
                    noteRepository.saveNote(note, tokenId)
                }
            }
        }
    }

    private fun getAccount() {
        viewModelScope.launch(Dispatchers.IO) {
            accountRepository.getAccount().collect { account ->
                withContext(Dispatchers.Main) {
                    _accountState.value = account
                }
            }
        }
    }

    private fun getTokenId(onCompleted: (tokenId: String) -> Unit) {
        if (_accountState.value.isLocalAccount()) {
            onCompleted(NoteRepository.LOCAL_TOKEN_ID)
        } else {
            viewModelScope.launch(Dispatchers.IO) {
                accountRepository.getToken { tokenResult ->
                    val tokenId = tokenResult.getOrDefault(NoteRepository.NO_TOKEN_ID)
                    onCompleted(tokenId)
                }
            }
        }
    }
}