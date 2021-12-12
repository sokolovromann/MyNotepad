package ru.sokolovromann.mynotepad.data.local.settings

data class Settings(
    val appNightTheme: Boolean = false,
    val notesSort: NotesSort = NotesSort.CREATED_ASC
)