package ru.sokolovromann.mynotepad.data.local.note

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable
import java.util.*

@Entity(tableName = "notes")
@Serializable
data class Note(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,

    @ColumnInfo(name = "uid")
    val uid: String = UUID.randomUUID().toString(),

    @ColumnInfo(name = "owner")
    val owner: String = "",

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "text")
    val text: String,

    @ColumnInfo(name = "created")
    val created: Long = System.currentTimeMillis(),

    @ColumnInfo(name = "last_modified")
    val lastModified: Long = System.currentTimeMillis(),

    @ColumnInfo(name = "sync_state")
    val syncState: String = NoteSyncState.SAVE.name
) {

    fun isEmpty(): Boolean {
        return id == 0L
    }

    fun isNotEmpty(): Boolean {
        return !isEmpty()
    }

    companion object {
        val EMPTY: Note = Note(title = "", text = "")
    }
}