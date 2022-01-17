package ru.sokolovromann.mynotepad.data.remote.note

import kotlinx.serialization.Serializable

@Serializable
data class NoteRequest(
    val uid: String,
    val owner: String,
    val title: String,
    val text: String,
    val created: Long,
    val lastModified: Long
)