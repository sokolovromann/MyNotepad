package ru.sokolovromann.mynotepad.data.repository

import kotlinx.coroutines.flow.Flow
import ru.sokolovromann.mynotepad.data.local.account.Account

interface AccountRepository {

    suspend fun getAccount(): Flow<Account>

    fun syncAccount(onResult: (result: Result<Account>) -> Unit)

    fun signUpWithEmailPassword(email: String, password: String, onResult: (result: Result<Unit>) -> Unit)

    fun signInWithEmailPassword(email: String, password: String, onResult: (result: Result<Unit>) -> Unit)

    fun updateEmail(email: String, onResult: (result: Result<Unit>) -> Unit)

    fun updatePassword(password: String, onResult: (result: Result<Unit>) -> Unit)

    fun sendPasswordResetEmail(email: String, onResult: (result: Result<Unit>) -> Unit)

    fun signOut(onResult: (result: Result<Unit>) -> Unit)

    fun deleteAccount(onResult: (result: Result<Unit>) -> Unit)

    fun continueWithoutSignIn(onResult: (result: Result<Unit>) -> Unit)
}