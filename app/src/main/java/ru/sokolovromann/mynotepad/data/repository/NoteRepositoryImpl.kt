package ru.sokolovromann.mynotepad.data.repository

import androidx.work.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.json.JSONException
import ru.sokolovromann.mynotepad.data.exception.IncorrectDataException
import ru.sokolovromann.mynotepad.data.local.note.Note
import ru.sokolovromann.mynotepad.data.local.note.NoteDao
import ru.sokolovromann.mynotepad.data.local.note.NoteSyncState
import ru.sokolovromann.mynotepad.data.mapping.NoteMapping
import ru.sokolovromann.mynotepad.data.remote.note.NoteApi
import ru.sokolovromann.mynotepad.data.worker.SyncRemoteNotesWorker
import ru.sokolovromann.mynotepad.data.worker.SyncLocalNotesWorker
import javax.inject.Inject

class NoteRepositoryImpl @Inject constructor(
    private val noteDao: NoteDao,
    private val noteApi: NoteApi,
    private val noteMapping: NoteMapping,
    private val workManager: WorkManager
) : NoteRepository {

    override fun scheduleSyncNotes() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val putNotesWorkRequest: OneTimeWorkRequest = OneTimeWorkRequestBuilder<SyncLocalNotesWorker>()
            .addTag(NoteRepository.SYNC_NOTES_WORK_TAG)
            .setConstraints(constraints)
            .build()

        val getNotesWorkRequest: OneTimeWorkRequest = OneTimeWorkRequestBuilder<SyncRemoteNotesWorker>()
            .addTag(NoteRepository.SYNC_NOTES_WORK_TAG)
            .setConstraints(constraints)
            .build()

        workManager
            .beginWith(putNotesWorkRequest)
            .then(getNotesWorkRequest)
            .enqueue()
    }

    override fun cancelSyncNotes() {
        workManager.cancelAllWorkByTag(NoteRepository.SYNC_NOTES_WORK_TAG)
    }

    override suspend fun syncRemoteNotesOrThrow(owner: String, tokenId: String) {
        noteApi.getNotes(owner, tokenId)
            .onSuccess { notesResponse -> replaceLocalNotes(noteMapping.toNotesList(notesResponse)) }
            .onFailure { exception -> throwException(exception) }
    }

    override suspend fun syncLocalNotesOrThrow(owner: String, tokenId: String) {
        val notes = noteDao.getNotes()
        notes.forEach { localNote ->
            when (NoteSyncState.valueOf(localNote.syncState)) {
                NoteSyncState.SAVE -> noteApi.putNote(noteMapping.toNoteRequest(localNote), tokenId)
                    .onSuccess { saveLocalNote(localNote.copyWithNothingSyncState()) }
                    .onFailure { exception -> throwException(exception) }

                NoteSyncState.DELETE -> noteApi.deleteNote(noteMapping.toNoteRequest(localNote), tokenId)
                    .onSuccess { deleteLocalNote(localNote) }
                    .onFailure { exception -> throwException(exception) }

                else -> {
                    // Nothing
                }
            }
        }
    }

    override suspend fun getNotes(): Flow<List<Note>> {
        return noteDao.getNotesExceptSyncState()
    }

    override suspend fun getNoteByUid(uid: String): Flow<Note> {
        val note = noteDao.getNoteByUid(uid) ?: Note.EMPTY
        return flowOf(note)
    }

    override suspend fun saveNote(note: Note, tokenId: String) {
        when (tokenId) {
            NoteRepository.LOCAL_TOKEN_ID -> saveLocalNote(note)

            NoteRepository.NO_TOKEN_ID -> {
                // Nothing
            }

            else -> noteApi.putNote(noteMapping.toNoteRequest(note), tokenId)
                .onSuccess { saveLocalNote(note.copyWithNothingSyncState()) }
                .onFailure { scheduleSyncNotes() }
        }
    }

    override suspend fun updateSyncStates(syncState: NoteSyncState) {
        noteDao.updateSyncStates(syncState.name)
    }

    override suspend fun prepareNotesForSync(syncState: NoteSyncState, owner: String) {
        noteDao.prepareNotesForSync(syncState.name, owner)
    }

    override suspend fun deleteNote(note: Note, tokenId: String) {
        when (tokenId) {
            NoteRepository.LOCAL_TOKEN_ID -> deleteLocalNote(note)

            NoteRepository.NO_TOKEN_ID -> {
                // Nothing
            }

            else -> noteApi.deleteNote(noteMapping.toNoteRequest(note), tokenId)
                .onSuccess { deleteLocalNote(note) }
                .onFailure { scheduleSyncNotes() }
        }
    }

    override suspend fun clearNotes(owner: String, tokenId: String) {
        when (tokenId) {
            NoteRepository.LOCAL_TOKEN_ID -> clearLocalNotes()

            NoteRepository.NO_TOKEN_ID -> {
                // Nothing
            }

            else -> noteApi.clearNotes(owner, tokenId)
                .onSuccess { clearLocalNotes() }
                .onFailure { scheduleSyncNotes() }
        }
    }

    private fun saveLocalNote(note: Note) {
        if (note.id == 0L) {
            noteDao.insertNote(note)
        } else {
            noteDao.updateNote(note)
        }
    }

    private fun deleteLocalNote(note: Note) {
        noteDao.deleteNote(note)
    }

    private fun clearLocalNotes() {
        noteDao.clearNotes()
    }

    private fun replaceLocalNotes(notes: List<Note>) {
        noteDao.clearNotes()
        notes.forEach { note -> noteDao.insertNote(note) }
    }

    private fun throwException(throwable: Throwable) {
        throw when (throwable) {
            is JSONException -> IncorrectDataException(throwable.message ?: "Json Exception")
            else -> Exception(throwable.message)
        }
    }

    private fun Note.copyWithNothingSyncState(): Note {
        return Note(id, uid, owner, title, text, created, lastModified, NoteSyncState.NOTHING.name)
    }
}