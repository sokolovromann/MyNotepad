package ru.sokolovromann.mynotepad.screens.addeditnote

sealed class AddEditNoteEvent {
    data class OnTitleChange(val newTitle: String) : AddEditNoteEvent()
    data class OnTextChange(val newText: String) : AddEditNoteEvent()
    object BackClick : AddEditNoteEvent()
    object SaveNoteClick : AddEditNoteEvent()
    data class OnAddEditNoteMenuChange(val isShowMenu: Boolean) : AddEditNoteEvent()
    object DeleteNoteClick : AddEditNoteEvent()
}
