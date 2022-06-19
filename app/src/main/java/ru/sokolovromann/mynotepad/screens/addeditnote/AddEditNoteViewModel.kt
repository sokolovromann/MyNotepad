package ru.sokolovromann.mynotepad.screens.addeditnote

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.sokolovromann.mynotepad.data.local.account.Account
import ru.sokolovromann.mynotepad.data.local.note.Note
import ru.sokolovromann.mynotepad.data.local.note.NoteSyncState
import ru.sokolovromann.mynotepad.data.repository.AccountRepository
import ru.sokolovromann.mynotepad.data.repository.NoteRepository
import ru.sokolovromann.mynotepad.data.repository.SettingsRepository
import ru.sokolovromann.mynotepad.screens.ScreensEvent
import ru.sokolovromann.mynotepad.screens.addeditnote.state.NoteTextFieldState
import ru.sokolovromann.mynotepad.screens.addeditnote.state.NoteTitleFieldState
import javax.inject.Inject

@HiltViewModel
class AddEditNoteViewModel @Inject constructor(
    private val noteRepository: NoteRepository,
    private val accountRepository: AccountRepository,
    private val settingsRepository: SettingsRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel(), ScreensEvent<AddEditNoteEvent> {

    private val _noteTitleFieldState: MutableState<NoteTitleFieldState> = mutableStateOf(NoteTitleFieldState.Default)
    val noteTitleFieldState: State<NoteTitleFieldState> = _noteTitleFieldState

    private val _noteTextFieldState: MutableState<NoteTextFieldState> = mutableStateOf(NoteTextFieldState.Default)
    val noteTextFieldState: State<NoteTextFieldState> = _noteTextFieldState

    private val _accountState: MutableState<Account> = mutableStateOf(Account.LocalAccount)
    val accountState: State<Account> = _accountState

    private val _addEditNoteMenuState: MutableState<Boolean> = mutableStateOf(false)
    val addEditNoteMenuState: State<Boolean> = _addEditNoteMenuState

    private val _lastDeletedNoteState: MutableState<Note> = mutableStateOf(Note.EMPTY)
    val lastDeletedNoteState: State<Note> = _lastDeletedNoteState

    private val _notesSaveAndCloseState: MutableState<Boolean> = mutableStateOf(false)
    val notesSaveAndCloseState: State<Boolean> = _notesSaveAndCloseState

    private val _addEditNoteUiEvent: MutableSharedFlow<AddEditNoteUiEvent> = MutableSharedFlow()
    val addEditNoteUiEvent: SharedFlow<AddEditNoteUiEvent> = _addEditNoteUiEvent

    private var originalNote: Note? = null

    init {
        val uid = savedStateHandle.get<String>("uid")
        if (uid == null || uid.isEmpty()) {
            initNote()
            viewModelScope.launch {
                _addEditNoteUiEvent.emit(AddEditNoteUiEvent.ShowKeyboard)
            }
        } else {
            loadNote(uid)
            viewModelScope.launch {
                _addEditNoteUiEvent.emit(AddEditNoteUiEvent.HideKeyboard)
            }
        }
        _lastDeletedNoteState.value = Note.EMPTY

        getAccount()
        getNotesSaveAndClose()
    }

    override fun onEvent(event: AddEditNoteEvent) {
        when (event) {
            is AddEditNoteEvent.OnTitleChange -> _noteTitleFieldState.value = _noteTitleFieldState.value.copy(
                title = event.newTitle,
                showLabel = event.newTitle.isEmpty()
            )

            is AddEditNoteEvent.OnTextChange -> _noteTextFieldState.value = _noteTextFieldState.value.copy(
                text = event.newText,
                showLabel = event.newText.isEmpty(),
                showError = false
            )

            AddEditNoteEvent.BackClick -> viewModelScope.launch {
                _lastDeletedNoteState.value = Note.EMPTY
                _addEditNoteUiEvent.emit(AddEditNoteUiEvent.HideKeyboard)
                _addEditNoteUiEvent.emit(AddEditNoteUiEvent.OpenNotes)
            }

            AddEditNoteEvent.SaveNoteClick -> if (isCorrectNote()) {
                saveNote()
            }

            is AddEditNoteEvent.OnAddEditNoteMenuChange -> _addEditNoteMenuState.value = event.isShowMenu

            AddEditNoteEvent.DeleteNoteClick -> deleteNote()
        }
    }

    private fun initNote(note: Note? = null) {
        if (note == null) {
            _noteTitleFieldState.value = NoteTitleFieldState.Default
            _noteTextFieldState.value = NoteTextFieldState.Default
        } else {
            _noteTitleFieldState.value = NoteTitleFieldState(
                title = note.title,
                showLabel = note.title.isEmpty()
            )

            _noteTextFieldState.value = NoteTextFieldState(
                text = note.text,
                showLabel = note.text.isEmpty(),
                showError = false
            )
        }

        originalNote = note
    }

    private fun loadNote(uid: String) {
        viewModelScope.launch(Dispatchers.IO) {
            noteRepository.getNoteByUid(uid).collect { note ->
                withContext(Dispatchers.Main) {
                    initNote(note)
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

    private fun getNotesSaveAndClose() {
        viewModelScope.launch(Dispatchers.IO) {
            settingsRepository.getNotesSaveAndClose().collect { notesSaveAndClose ->
                withContext(Dispatchers.Main) {
                    _notesSaveAndCloseState.value = notesSaveAndClose
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

    private fun isCorrectNote(): Boolean {
        val isCorrect = _noteTextFieldState.value.text.isNotEmpty()
        _noteTextFieldState.value = _noteTextFieldState.value.copy(
            showError = !isCorrect
        )
        return isCorrect
    }

    private fun saveNote() {
        val noteSyncState = if (_accountState.value.isLocalAccount()) {
            NoteSyncState.NO_SYNC.name
        } else {
            NoteSyncState.SAVE.name
        }

        val note = originalNote?.copy(
            title = _noteTitleFieldState.value.title,
            text = _noteTextFieldState.value.text,
            lastModified = System.currentTimeMillis(),
            syncState = noteSyncState
        ) ?: Note(
            owner = _accountState.value.uid,
            title = _noteTitleFieldState.value.title,
            text = _noteTextFieldState.value.text,
            syncState = noteSyncState
        )

        getTokenId { tokenId ->
            viewModelScope.launch(Dispatchers.IO) {
                noteRepository.saveNote(note, tokenId) { note ->
                    originalNote = note
                }

                withContext(Dispatchers.Main) {
                    if (_notesSaveAndCloseState.value) {
                        _addEditNoteUiEvent.emit(AddEditNoteUiEvent.OpenNotesAfterSaved)
                    } else {
                        _addEditNoteUiEvent.emit(AddEditNoteUiEvent.ShowSavedMessage)
                    }
                }
            }
        }
    }

    private fun deleteNote() {
        _lastDeletedNoteState.value = originalNote ?: Note.EMPTY

        if (originalNote == null) {
            viewModelScope.launch {
                _addEditNoteUiEvent.emit(AddEditNoteUiEvent.OpenNotes)
            }
        } else {
            getTokenId { tokenId ->
                viewModelScope.launch(Dispatchers.IO) {
                    noteRepository.deleteNote(originalNote!!, tokenId)
                    withContext(Dispatchers.Main) {
                        _addEditNoteUiEvent.emit(AddEditNoteUiEvent.OpenNotesAfterDeleted)
                    }
                }
            }
        }
    }
}