package ru.sokolovromann.mynotepad.data.repository

import kotlinx.coroutines.flow.Flow
import ru.sokolovromann.mynotepad.data.local.note.Note

interface NoteRepository {

    suspend fun getNotes(): Flow<List<Note>>

    suspend fun getNoteByUid(uid: String): Flow<Note>

    suspend fun saveNote(note: Note)

    suspend fun deleteNote(note: Note)
}