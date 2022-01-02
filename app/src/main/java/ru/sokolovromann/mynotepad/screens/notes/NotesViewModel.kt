package ru.sokolovromann.mynotepad.screens.notes

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.sokolovromann.mynotepad.data.local.account.Account
import ru.sokolovromann.mynotepad.data.local.note.Note
import ru.sokolovromann.mynotepad.data.repository.AccountRepository
import ru.sokolovromann.mynotepad.data.repository.NoteRepository
import ru.sokolovromann.mynotepad.screens.ScreensEvent
import javax.inject.Inject

@HiltViewModel
class NotesViewModel @Inject constructor(
    private val noteRepository: NoteRepository,
    private val accountRepository: AccountRepository
) : ViewModel(), ScreensEvent<NotesEvent> {

    private val _notesState: MutableState<NotesState> = mutableStateOf(NotesState.Loading)
    val notesState: State<NotesState> = _notesState

    private val _noteMenuState: MutableState<Int> = mutableStateOf(-1)
    val noteMenuState: State<Int> = _noteMenuState

    private val _accountNameState: MutableState<String> = mutableStateOf(Account.DEFAULT_NAME)
    val accountNameState: State<String> = _accountNameState

    private val _notesUiEvent: MutableSharedFlow<NotesUiEvent> = MutableSharedFlow()
    val notesUiEvent: SharedFlow<NotesUiEvent> = _notesUiEvent

    private var lastDeletedNote: Note? = null

    init {
        getNotes()
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

            is NotesEvent.NavigationMenuStateChange -> viewModelScope.launch {
                if (event.isOpen) {
                    _notesUiEvent.emit(NotesUiEvent.OpenNavigationMenu)
                } else {
                    _notesUiEvent.emit(NotesUiEvent.CloseNavigationMenu)
                }
            }
        }
    }

    private fun getNotes() {
        _notesState.value = NotesState.Loading

        viewModelScope.launch(Dispatchers.IO) {
            noteRepository.getNotes().collect { notes ->
                withContext(Dispatchers.Main) {
                    if (notes.isEmpty()) {
                        _notesState.value = NotesState.NotFound
                    } else {
                        _notesState.value = NotesState.Notes(notes = notes)
                    }
                }
            }
        }
    }

    private fun deleteNote(note: Note) {
        viewModelScope.launch(Dispatchers.IO) {
            noteRepository.deleteNote(note)

            withContext(Dispatchers.Main) {
                lastDeletedNote = note
                _noteMenuState.value = -1
                _notesUiEvent.emit(NotesUiEvent.ShowDeletedMessage)
            }
        }
    }

    private fun restoreLastNote() {
        viewModelScope.launch(Dispatchers.IO) {
            lastDeletedNote?.let {
                val note = it.copy(id = 0L)
                noteRepository.saveNote(note)
            }
        }
    }

    private fun getAccount() {
        viewModelScope.launch(Dispatchers.IO) {
            accountRepository.getAccount().collect { account ->
                withContext(Dispatchers.Main) {
                    _accountNameState.value = account.getName()
                }
            }
        }
    }
}