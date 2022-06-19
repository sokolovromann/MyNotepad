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
import ru.sokolovromann.mynotepad.data.local.settings.Settings
import ru.sokolovromann.mynotepad.data.repository.AccountRepository
import ru.sokolovromann.mynotepad.data.repository.NoteRepository
import ru.sokolovromann.mynotepad.data.repository.SettingsRepository
import ru.sokolovromann.mynotepad.screens.ScreensEvent
import ru.sokolovromann.mynotepad.screens.notes.state.NotesItemState
import javax.inject.Inject

@HiltViewModel
class NotesViewModel @Inject constructor(
    private val noteRepository: NoteRepository,
    private val accountRepository: AccountRepository,
    private val settingsRepository: SettingsRepository
) : ViewModel(), ScreensEvent<NotesEvent> {

    private val _notesItemsState: MutableState<List<NotesItemState>> = mutableStateOf(listOf())
    val notesItemsState: State<List<NotesItemState>> = _notesItemsState

    private val _loadingState: MutableState<Boolean> = mutableStateOf(false)
    val loadingState: State<Boolean> = _loadingState

    private val _notesSortState: MutableState<NotesSort> = mutableStateOf(NotesSort.CREATED_ASC)
    val notesSortState: State<NotesSort> = _notesSortState

    private val _notesMultiColumnsState: MutableState<Boolean> = mutableStateOf(false)
    val notesMultiColumnsState: State<Boolean> = _notesMultiColumnsState

    private val _accountState: MutableState<Account> = mutableStateOf(Account.LocalAccount)
    val accountState: State<Account> = _accountState

    private val _notesUiEvent: MutableSharedFlow<NotesUiEvent> = MutableSharedFlow()
    val notesUiEvent: SharedFlow<NotesUiEvent> = _notesUiEvent

    private var lastDeletedNote: Note? = null

    init {
        getData()
    }

    override fun onEvent(event: NotesEvent) {
        when (event) {
            NotesEvent.AddNoteClick -> viewModelScope.launch {
                _notesUiEvent.emit(NotesUiEvent.AddNote)
            }

            is NotesEvent.NoteClick -> viewModelScope.launch {
                _notesUiEvent.emit(NotesUiEvent.EditNote(event.note))
            }

            is NotesEvent.OnNoteMenuChange -> showOrHideNoteMenu(event.isShow, event.note)

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

            is NotesEvent.NotesMultiColumnsClick -> saveNotesMultiColumns()

            is NotesEvent.RefreshNotesClick -> getData()

            is NotesEvent.NoteDeleted -> if (event.deletedNote.isNotEmpty()) {
                lastDeletedNote = event.deletedNote
                viewModelScope.launch(Dispatchers.Main) {
                    _notesUiEvent.emit(NotesUiEvent.ShowDeletedMessage)
                }
            }

            is NotesEvent.NoteSaved -> if (event.noteSaved) {
                viewModelScope.launch(Dispatchers.Main) {
                    _notesUiEvent.emit(NotesUiEvent.ShowSavedMessage)
                }
            }
        }
    }

    private fun getData() {
        getAccount()
        getSettings {
            getNotes(it)
        }
    }

    private fun getSettings(onCompleted: (Settings) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            settingsRepository.getSettings().collect { settings ->
                withContext(Dispatchers.Main) {
                    _notesSortState.value = settings.notesSort
                    _notesMultiColumnsState.value = settings.notesMultiColumns
                    onCompleted(settings)
                }
            }
        }
    }

    private fun showOrHideNoteMenu(showMenu: Boolean, note: Note) {
        _notesItemsState.value.find { it.note == note }?.let { state ->
            val index = _notesItemsState.value.indexOf(state)

            val notes = _notesItemsState.value.toMutableList()
            notes[index] = state.copy(showMenu = showMenu)

            _notesItemsState.value = notes
        }
    }

    private fun saveNotesSort(notesSort: NotesSort) {
        viewModelScope.launch(Dispatchers.IO) {
            settingsRepository.saveNotesSort(notesSort)
        }
    }

    private fun saveNotesMultiColumns() {
        viewModelScope.launch(Dispatchers.IO) {
            settingsRepository.saveNotesMultiColumns(!_notesMultiColumnsState.value)
        }
    }

    private fun getNotes(settings: Settings) {
        _loadingState.value = true

        val nextSync = settings.notesLastSync + settings.notesSyncPeriod.millis
        if (nextSync < System.currentTimeMillis()) {
            syncNotes { _loadingState.value = false }
        } else {
            getLocalNotes { _loadingState.value = false }
        }
    }

    private fun syncNotes(onCompleted: () -> Unit) {
        noteRepository.scheduleSyncNotes()
        viewModelScope.launch(Dispatchers.IO) {
            settingsRepository.saveNotesLastSync(System.currentTimeMillis())
            noteRepository.getNotes().collect { notes ->
                showNotes(notes)
                onCompleted()
            }
        }
    }

    private fun getLocalNotes(onCompleted: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            noteRepository.getNotes().collect { notes ->
                showNotes(notes)
                onCompleted()
            }
        }
    }

    private suspend fun showNotes(notes: List<Note>) = withContext(Dispatchers.Main) {
        _notesItemsState.value = sortNotes(notes).map { note ->
            NotesItemState(
                note = note,
                showMenu = false,
                maxLines = if (_notesMultiColumnsState.value) 15 else 10
            )
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
                    noteRepository.saveNote(note, tokenId) {}
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
                    val tokenId = tokenResult.getOrDefault(NoteRepository.LOCAL_TOKEN_ID)
                    onCompleted(tokenId)
                }
            }
        }
    }
}