package ru.sokolovromann.mynotepad.data.remote.note

data class NoteAccessRequest(
    val userUid: String,
    val tokenId: String
)
