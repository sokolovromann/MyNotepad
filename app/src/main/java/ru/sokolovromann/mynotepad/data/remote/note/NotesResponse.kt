package ru.sokolovromann.mynotepad.data.remote.note

import kotlinx.serialization.Serializable

@Serializable
data class NotesResponse(
    val notes: List<NoteResponse>
)
