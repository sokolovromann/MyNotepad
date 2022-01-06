package ru.sokolovromann.mynotepad.data.remote.note

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*
import ru.sokolovromann.mynotepad.data.remote.HttpRoute
import javax.inject.Inject

class NoteApi @Inject constructor(
    private val client: HttpClient
) {

    suspend fun getNotes(accessRequest: NoteAccessRequest): Result<NotesResponse> {
        return try {
            val response: Map<String, NoteResponse> = client.get{
                url(HttpRoute.notes(accessRequest.userUid, accessRequest.tokenId))
            }
            val notesResponse = NotesResponse(response.values.toList())
            Result.success(notesResponse)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun putNote(accessRequest: NoteAccessRequest, noteRequest: NoteRequest): Result<NoteResponse> {
        return try {
            Result.success(
                client.put {
                    url(HttpRoute.note(noteRequest.uid, accessRequest.userUid, accessRequest.tokenId))
                    contentType(ContentType.Application.Json)
                    body = noteRequest
                }
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteNote(accessRequest: NoteAccessRequest, noteRequest: NoteRequest): Result<Unit> {
        return try {
            Result.success(
                client.delete(
                    urlString = HttpRoute.note(noteRequest.uid, accessRequest.userUid, accessRequest.tokenId)
                )
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}