package ru.sokolovromann.mynotepad.data.local.note

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

    @Query("SELECT * FROM notes ORDER BY created ASC")
    fun getNotes(): Flow<List<Note>>

    @Query("SELECT * FROM notes WHERE uid = :uid")
    fun getNoteByUid(uid: String): Flow<Note>

    @Insert
    fun insertNote(note: Note): Long

    @Update
    fun updateNote(note: Note): Int

    @Delete
    fun deleteNote(note: Note): Int
}