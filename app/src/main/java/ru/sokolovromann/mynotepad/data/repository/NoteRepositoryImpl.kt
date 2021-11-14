package ru.sokolovromann.mynotepad.data.repository

import kotlinx.coroutines.flow.Flow
import ru.sokolovromann.mynotepad.data.local.LocalDatabase
import ru.sokolovromann.mynotepad.data.local.note.Note
import javax.inject.Inject

class NoteRepositoryImpl @Inject constructor(
    private val localDatabase: LocalDatabase
) : NoteRepository {

    override suspend fun getNotes(): Flow<List<Note>> {
        return localDatabase.noteDao().getNotes()
    }

    override suspend fun getNoteByUid(uid: String): Flow<Note> {
        return localDatabase.noteDao().getNoteByUid(uid)
    }

    override suspend fun saveNote(note: Note) {
        if (note.id == 0L) {
            localDatabase.noteDao().insertNote(note)
        } else {
            localDatabase.noteDao().updateNote(note)
        }
    }

    override suspend fun deleteNote(note: Note) {
        localDatabase.noteDao().deleteNote(note)
    }
}