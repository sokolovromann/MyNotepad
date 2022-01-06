package ru.sokolovromann.mynotepad.data.remote.note

import kotlinx.serialization.Serializable

@Serializable
data class NoteResponse(
    val uid: String,
    val title: String,
    val text: String,
    val created: Long,
    val lastModified: Long
)
