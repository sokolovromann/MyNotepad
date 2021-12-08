package ru.sokolovromann.mynotepad.screens.addeditnote

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.sokolovromann.mynotepad.data.local.note.Note
import ru.sokolovromann.mynotepad.data.repository.NoteRepository
import ru.sokolovromann.mynotepad.screens.ScreensEvent
import javax.inject.Inject

@HiltViewModel
class AddEditNoteViewModel @Inject constructor(
    private val noteRepository: NoteRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel(), ScreensEvent<AddEditNoteEvent> {

    private val _addEditNoteState: MutableState<AddEditNoteState> = mutableStateOf(AddEditNoteState())
    val addEditNoteState: State<AddEditNoteState> = _addEditNoteState

    private val _addEditNoteUiEvent: MutableSharedFlow<AddEditNoteUiEvent> = MutableSharedFlow()
    val addEditNoteUiEvent: SharedFlow<AddEditNoteUiEvent> = _addEditNoteUiEvent

    private var originalNote: Note? = null

    init {
        val uid = savedStateHandle.get<String>("uid")
        if (uid == null || uid.isEmpty()) {
            initNote()
        } else {
            loadNote(uid)
        }
    }

    override fun onEvent(event: AddEditNoteEvent) {
        when (event) {
            is AddEditNoteEvent.OnTitleChange -> _addEditNoteState.value = _addEditNoteState.value.copy(
                title = event.newTitle
            )
            is AddEditNoteEvent.OnTextChange -> _addEditNoteState.value = _addEditNoteState.value.copy(
                text = event.newText
            )
            AddEditNoteEvent.BackClick -> viewModelScope.launch {
                _addEditNoteUiEvent.emit(AddEditNoteUiEvent.OpenNotes)
            }
            AddEditNoteEvent.SaveNoteClick -> saveNote()
        }
    }

    private fun initNote(note: Note? = null) {
        _addEditNoteState.value = AddEditNoteState(
            title = note?.title ?: "",
            text = note?.text ?: "",
            emptyTextError = false
        )
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

    private fun saveNote() {
        val note = originalNote?.copy(
            title = _addEditNoteState.value.title,
            text = _addEditNoteState.value.text,
            lastModified = System.currentTimeMillis()
        ) ?: Note(
            title = _addEditNoteState.value.title,
            text = _addEditNoteState.value.text
        )

        if (note.text.isEmpty()) {
            _addEditNoteState.value = _addEditNoteState.value.copy(emptyTextError = true)
        } else {
            viewModelScope.launch(Dispatchers.IO) {
                noteRepository.saveNote(note)
                withContext(Dispatchers.Main) {
                    _addEditNoteUiEvent.emit(AddEditNoteUiEvent.ShowSavedMessage)
                }
            }
        }
    }
}