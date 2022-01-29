package ru.sokolovromann.mynotepad.data.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import ru.sokolovromann.mynotepad.data.exception.IncorrectDataException
import ru.sokolovromann.mynotepad.data.exception.NetworkException
import ru.sokolovromann.mynotepad.data.local.account.Account
import ru.sokolovromann.mynotepad.data.repository.AccountRepository
import ru.sokolovromann.mynotepad.data.repository.NoteRepository

class SyncLocalNotesWorker(
    context: Context,
    workerParameters: WorkerParameters
) : CoroutineWorker(context, workerParameters) {

    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface SyncLocalNotesWorkerEntryPoint {
        fun accountRepository(): AccountRepository
        fun noteRepository(): NoteRepository
        fun dispatcher(): CoroutineDispatcher
    }

    private val hiltEntryPoint = EntryPointAccessors.fromApplication(
        applicationContext,
        SyncLocalNotesWorkerEntryPoint::class.java
    )

    private val dispatcher = hiltEntryPoint.dispatcher()
    private val accountRepository = hiltEntryPoint.accountRepository()
    private val noteRepository = hiltEntryPoint.noteRepository()

    override suspend fun doWork(): Result = try {
        getAccess { owner, tokenId ->
            syncLocalNotes(owner, tokenId)
        }
        Result.success()
    } catch (exception: NetworkException) {
        Result.retry()
    } catch (exception: IncorrectDataException) {
        Result.failure()
    } catch (exception: Exception) {
        Result.failure()
    }

    private fun getAccess(onCompleted: (owner: String, tokenId: String) -> Unit) {
        getAccount { account ->
            getTokenId { tokenId ->
                onCompleted(account.uid, tokenId)
            }
        }
    }

    private fun getAccount(onCompleted: (account: Account) -> Unit) {
        runBlocking(dispatcher) {
            val account = accountRepository.getAccount().firstOrNull() ?: Account.LocalAccount
            onCompleted(account)
        }
    }

    private fun getTokenId(onCompleted: (tokenId: String) -> Unit) {
        runBlocking(dispatcher) {
            accountRepository.getToken { tokenResult ->
                val tokenId = tokenResult.getOrThrow()
                onCompleted(tokenId)
            }
        }
    }

    private fun syncLocalNotes(owner: String, tokenId: String) {
        runBlocking(dispatcher) {
            noteRepository.syncLocalNotesOrThrow(owner, tokenId)
        }
    }
}