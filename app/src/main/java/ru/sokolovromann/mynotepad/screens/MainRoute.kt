package ru.sokolovromann.mynotepad.screens

sealed class MainRoute(val route: String) {
    object Notes : MainRoute("notes")
    object AddNote : MainRoute("addnote")
    data class EditNote(val uid: String) : MainRoute("editnote/$uid")
}
