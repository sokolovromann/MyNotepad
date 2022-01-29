package ru.sokolovromann.mynotepad.data.repository

import kotlinx.coroutines.flow.Flow
import ru.sokolovromann.mynotepad.data.local.note.Note
import ru.sokolovromann.mynotepad.data.local.note.NoteSyncState

interface NoteRepository {

    companion object {
        const val NO_TOKEN_ID: String = ""
        const val LOCAL_TOKEN_ID: String = "local_token_id"

        const val SYNC_NOTES_WORK_TAG = "sync_notes_work"
    }

    fun scheduleSyncNotes()

    fun cancelSyncNotes()

    suspend fun syncRemoteNotesOrThrow(owner: String, tokenId: String)

    suspend fun syncLocalNotesOrThrow(owner: String, tokenId: String)

    suspend fun getNotes(): Flow<List<Note>>

    suspend fun getNoteByUid(uid: String): Flow<Note>

    suspend fun saveNote(note: Note, tokenId: String)

    suspend fun updateSyncStates(syncState: NoteSyncState)

    suspend fun deleteNote(note: Note, tokenId: String)

    suspend fun clearNotes(owner: String, tokenId: String)
}