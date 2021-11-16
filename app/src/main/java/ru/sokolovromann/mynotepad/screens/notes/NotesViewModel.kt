package ru.sokolovromann.mynotepad.screens.notes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import ru.sokolovromann.mynotepad.data.repository.NoteRepository
import javax.inject.Inject

@HiltViewModel
class NotesViewModel @Inject constructor(
    private val noteRepository: NoteRepository
) : ViewModel() {

    private val _notesState: MutableLiveData<NotesState> = MutableLiveData(NotesState.Loading)
    val notesState: LiveData<NotesState> = _notesState

    fun getNotes() {
        viewModelScope.launch(Dispatchers.IO) {
            _notesState.postValue(NotesState.Loading)

            noteRepository.getNotes().collect { notes ->
                if (notes.isEmpty()) {
                    _notesState.postValue(NotesState.NotFound)
                } else {
                    _notesState.postValue(NotesState.Notes(notes))
                }
            }
        }
    }
}