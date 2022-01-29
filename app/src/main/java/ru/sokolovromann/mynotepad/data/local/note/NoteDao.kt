package ru.sokolovromann.mynotepad.data.local.note

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

    @Query("SELECT * FROM notes ORDER BY created ASC")
    fun getNotes(): List<Note>

    @Query("SELECT * FROM notes WHERE sync_state != :exceptSyncState ORDER BY created ASC")
    fun getNotesExceptSyncState(exceptSyncState: String = NoteSyncState.DELETE.name): Flow<List<Note>>

    @Query("SELECT * FROM notes WHERE uid = :uid")
    fun getNoteByUid(uid: String): Note?

    @Insert
    fun insertNote(note: Note): Long

    @Update
    fun updateNote(note: Note): Int

    @Query("UPDATE notes SET sync_state = :syncState WHERE uid = :uid")
    fun updateSyncStateByUid(uid: String, syncState: String)

    @Query("UPDATE notes SET sync_state = :syncState")
    fun updateSyncStates(syncState: String = NoteSyncState.DELETE.name)

    @Delete
    fun deleteNote(note: Note): Int

    @Query("DELETE FROM notes")
    fun clearNotes()
}