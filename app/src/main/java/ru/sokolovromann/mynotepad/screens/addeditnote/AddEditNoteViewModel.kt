package ru.sokolovromann.mynotepad.screens.addeditnote

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.sokolovromann.mynotepad.data.local.note.Note
import ru.sokolovromann.mynotepad.data.repository.NoteRepository
import javax.inject.Inject

@HiltViewModel
class AddEditNoteViewModel @Inject constructor(
    private val noteRepository: NoteRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _addEditNoteState: MutableState<AddEditNoteState> = mutableStateOf(AddEditNoteState.Loading)
    val addEditNoteState: State<AddEditNoteState> = _addEditNoteState

    private val titleState: MutableState<String> = mutableStateOf("")

    private val textState: MutableState<String> = mutableStateOf("")

    private var originalNote: Note? = null

    init {
        val uid = savedStateHandle.get<String>("uid")
        if (uid == null || uid.isEmpty()) {
            initNote()
        } else {
            loadNote(uid)
        }
    }

    fun saveNote() {
        val note = originalNote?.copy(
            title = titleState.value,
            text = textState.value,
            lastModified = System.currentTimeMillis()
        ) ?: Note(
            title = titleState.value,
            text =  textState.value
        )

        viewModelScope.launch(Dispatchers.IO) {
            noteRepository.saveNote(note)
        }
    }

    private fun initNote(note: Note? = null) {
        titleState.value = note?.title ?: ""
        textState.value = note?.text ?: ""
        originalNote = note

        _addEditNoteState.value = AddEditNoteState.NoteDisplay(
            titleState = titleState,
            textState = textState,
            onTitleChange = { newTitle -> titleState.value = newTitle },
            onTextChange = { newText -> textState.value = newText }
        )
    }

    private fun loadNote(uid: String) {
        _addEditNoteState.value = AddEditNoteState.Loading

        viewModelScope.launch(Dispatchers.IO) {
            noteRepository.getNoteByUid(uid).collect { note ->
                withContext(Dispatchers.Main) {
                    initNote(note)
                }
            }
        }
    }
}