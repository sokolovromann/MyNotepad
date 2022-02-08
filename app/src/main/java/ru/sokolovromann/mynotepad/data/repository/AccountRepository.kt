package ru.sokolovromann.mynotepad.data.repository

import kotlinx.coroutines.flow.Flow
import ru.sokolovromann.mynotepad.data.local.account.Account

interface AccountRepository {

    suspend fun getAccount(): Flow<Account>

    suspend fun getToken(onResult: (result: Result<String>) -> Unit)

    fun syncAccount(onResult: (result: Result<Account>) -> Unit)

    fun signUpWithEmailPassword(email: String, password: String, onResult: (result: Result<Account>) -> Unit)

    fun signInWithEmailPassword(email: String, password: String, onResult: (result: Result<Account>) -> Unit)

    fun updateEmail(currentPassword: String, newEmail: String, onResult: (result: Result<Unit>) -> Unit)

    fun updatePassword(currentPassword: String, newPassword: String, onResult: (result: Result<Unit>) -> Unit)

    fun sendPasswordResetEmail(email: String, onResult: (result: Result<Unit>) -> Unit)

    fun signOut(onResult: (result: Result<Unit>) -> Unit)

    fun deleteAccount(currentPassword: String, onResult: (result: Result<Unit>) -> Unit)

    fun continueWithoutSignIn(onResult: (result: Result<Unit>) -> Unit)
}