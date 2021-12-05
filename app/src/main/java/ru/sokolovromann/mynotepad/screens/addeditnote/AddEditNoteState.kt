package ru.sokolovromann.mynotepad.screens.addeditnote

import androidx.compose.runtime.State

sealed class AddEditNoteState {
    object Loading : AddEditNoteState()

    data class NoteDisplay(
        val titleState: State<String>,
        val textState: State<String>,
        val showEmptyNoteMessage: State<Boolean>,
        val onTitleChange: (newTitle: String) -> Unit,
        val onTextChange: (newText: String) -> Unit,
        val onShowEmptyNoteMessageChange: (isShow: Boolean) -> Unit
    ): AddEditNoteState()
}