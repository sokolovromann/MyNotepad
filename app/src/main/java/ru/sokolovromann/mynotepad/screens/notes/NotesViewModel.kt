package ru.sokolovromann.mynotepad.screens.notes

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.sokolovromann.mynotepad.data.local.note.Note
import ru.sokolovromann.mynotepad.data.repository.NoteRepository
import javax.inject.Inject

@HiltViewModel
class NotesViewModel @Inject constructor(
    private val noteRepository: NoteRepository
) : ViewModel() {

    private val _notesState: MutableLiveData<NotesState> = MutableLiveData(NotesState.Loading)
    val notesState: LiveData<NotesState> = _notesState

    private val _noteMenuPositionState: MutableState<Int> = mutableStateOf(-1)

    private var lastDeletedNote: Note? = null

    fun getNotes() {
        _notesState.value = NotesState.Loading

        viewModelScope.launch(Dispatchers.IO) {
            noteRepository.getNotes().collect { notes ->
                withContext(Dispatchers.Main) {
                    if (notes.isEmpty()) {
                        _notesState.value = NotesState.NotFound
                    } else {
                        _notesState.value = NotesState.Notes(
                            notes = notes,
                            noteMenuPosition = _noteMenuPositionState,
                            onNoteMenuPositionChange = { newPosition ->
                                _noteMenuPositionState.value = newPosition
                            },
                            onRestoreLastNote = { restoreLastNote() }
                        )
                    }
                }
            }
        }
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch(Dispatchers.IO) {
            noteRepository.deleteNote(note)

            withContext(Dispatchers.Main) {
                _noteMenuPositionState.value = -1
                lastDeletedNote = note
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
}