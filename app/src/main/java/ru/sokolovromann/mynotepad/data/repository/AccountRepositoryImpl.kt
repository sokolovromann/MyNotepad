package ru.sokolovromann.mynotepad.data.repository

import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import ru.sokolovromann.mynotepad.data.exception.IncorrectDataException
import ru.sokolovromann.mynotepad.data.exception.NetworkException
import ru.sokolovromann.mynotepad.data.local.account.Account
import ru.sokolovromann.mynotepad.data.local.account.AccountDataStore
import ru.sokolovromann.mynotepad.data.mapping.AccountMapping
import ru.sokolovromann.mynotepad.data.remote.auth.AuthApi
import ru.sokolovromann.mynotepad.data.remote.auth.UserResponse
import javax.inject.Inject

class AccountRepositoryImpl @Inject constructor(
    private val dataStore: AccountDataStore,
    private val api: AuthApi,
    private val accountMapping: AccountMapping,
    private val dispatcher: CoroutineDispatcher
) : AccountRepository {

    override suspend fun getAccount(): Flow<Account> {
        return dataStore.getAccount()
    }

    override suspend fun getToken(onResult: (result: Result<String>) -> Unit) {
        api.getTokenId { tokenResult ->
            tokenResult
                .onSuccess { tokenId -> onResult(Result.success(tokenId)) }
                .onFailure { exception -> onResult(failureResult(exception)) }
        }
    }

    override fun syncAccount(onResult: (result: Result<Account>) -> Unit) {
        api.getCurrentUser { userResult ->
            userResult
                .onSuccess { userResponse ->
                    cacheAccount(userResponse) { account -> onResult(Result.success(account)) }
                }
                .onFailure { exception ->
                    when (exception) {
                        is FirebaseNetworkException -> onResult(Result.failure(NetworkException()))
                        is FirebaseAuthException -> onResult(Result.failure(NetworkException()))
                        is NullPointerException -> onResult(Result.failure(exception))
                        else -> onResult(Result.failure(Exception(exception.message)))
                    }
                }
        }
    }

    override fun signUpWithEmailPassword(
        email: String,
        password: String,
        onResult: (result: Result<Account>) -> Unit
    ) {
        api.createUserWithEmailPassword(email, password) { userResult ->
            userResult
                .onSuccess { userResponse ->
                    cacheAccount(userResponse) { account -> onResult(Result.success(account)) }
                }
                .onFailure { exception ->
                    when (exception) {
                        is FirebaseNetworkException -> onResult(Result.failure(NetworkException()))
                        is FirebaseAuthInvalidCredentialsException -> onResult(Result.failure(IncorrectDataException()))
                        is FirebaseAuthException -> onResult(Result.failure(NetworkException()))
                        is NullPointerException -> onResult(Result.failure(exception))
                        else -> onResult(Result.failure(Exception(exception.message)))
                    }
                }
        }
    }

    override fun signInWithEmailPassword(
        email: String,
        password: String,
        onResult: (result: Result<Account>) -> Unit
    ) {
        api.signInWithEmailPassword(email, password) { userResult ->
            userResult
                .onSuccess { userResponse ->
                    cacheAccount(userResponse) { account -> onResult(Result.success(account)) }
                }
                .onFailure { exception ->
                    when (exception) {
                        is FirebaseNetworkException -> onResult(Result.failure(NetworkException()))
                        is FirebaseAuthInvalidCredentialsException -> onResult(Result.failure(IncorrectDataException()))
                        is FirebaseAuthException -> onResult(Result.failure(NetworkException()))
                        is NullPointerException -> onResult(Result.failure(exception))
                        else -> onResult(Result.failure(Exception(exception.message)))
                    }
                }
        }
    }

    override fun updateEmail(currentPassword: String, newEmail: String, onResult: (result: Result<Unit>) -> Unit) {
        val updateEmail = {
            api.updateEmail(newEmail) { userResult ->
                userResult
                    .onSuccess {
                        cacheEmail(newEmail) { onResult(Result.success(Unit)) }
                    }
                    .onFailure { onResult(failureResult(it)) }
            }
        }
        api.resignIn(currentPassword) { resignInResult ->
            resignInResult
                .onSuccess { updateEmail() }
                .onFailure { onResult(failureResult(it)) }
        }
    }

    override fun updatePassword(currentPassword: String, newPassword: String, onResult: (result: Result<Unit>) -> Unit) {
        val updatePassword = {
            api.updatePassword(newPassword) { userResult ->
                userResult
                    .onSuccess { onResult(Result.success(Unit)) }
                    .onFailure { onResult(failureResult(it)) }
            }
        }
        api.resignIn(currentPassword) { resignInResult ->
            resignInResult
                .onSuccess { updatePassword() }
                .onFailure { onResult(failureResult(it)) }
        }
    }

    override fun sendPasswordResetEmail(email: String, onResult: (result: Result<Unit>) -> Unit) {
        api.sendPasswordResetEmail(email) { userResult ->
            userResult
                .onSuccess {
                    onResult(Result.success(Unit))
                }
                .onFailure { exception ->
                    when (exception) {
                        is FirebaseNetworkException -> onResult(Result.failure(NetworkException()))
                        is FirebaseAuthException -> onResult(Result.failure(NetworkException()))
                        is NullPointerException -> onResult(Result.failure(exception))
                        else -> onResult(Result.failure(Exception(exception.message)))
                    }
                }
        }
    }

    override fun signOut(onResult: (result: Result<Unit>) -> Unit) {
        api.signOut { userResult ->
            userResult
                .onSuccess {
                    clearCache { onResult(Result.success(Unit)) }
                }
                .onFailure { exception ->
                    when (exception) {
                        is FirebaseNetworkException -> onResult(Result.failure(NetworkException()))
                        is FirebaseAuthException -> onResult(Result.failure(NetworkException()))
                        is NullPointerException -> onResult(Result.failure(exception))
                        else -> onResult(Result.failure(Exception(exception.message)))
                    }
                }
        }
    }

    override fun deleteAccount(currentPassword: String, onResult: (result: Result<Unit>) -> Unit) {
        val deleteCurrentUser = {
            api.deleteCurrentUser { userResult ->
                userResult
                    .onSuccess {
                        clearCache { onResult(Result.success(Unit)) }
                    }
                    .onFailure { onResult(failureResult(it)) }
            }
        }
        api.resignIn(currentPassword) { resignInResult ->
            resignInResult
                .onSuccess { deleteCurrentUser() }
                .onFailure { onResult(failureResult(it)) }
        }
    }

    override fun continueWithoutSignIn(onResult: (result: Result<Unit>) -> Unit) {
        runBlocking(dispatcher) {
            dataStore.saveAccount(Account.LocalAccount)
            onResult(Result.success(Unit))
        }
    }

    private fun cacheAccount(userResponse: UserResponse, onCached: (account: Account) -> Unit) {
        runBlocking(dispatcher) {
            val account = accountMapping.toAccount(userResponse)
            dataStore.saveAccount(account)
            onCached(account)
        }
    }

    private fun cacheEmail(email: String, onCached: () -> Unit) {
        runBlocking(dispatcher) {
            dataStore.saveEmail(email)
            onCached()
        }
    }

    private fun clearCache(onClear: () -> Unit) {
        runBlocking(dispatcher) {
            dataStore.clear()
            onClear()
        }
    }

    private fun<T> failureResult(throwable: Throwable): Result<T> {
        return when (throwable) {
            is FirebaseNetworkException -> Result.failure(NetworkException())
            is FirebaseAuthRecentLoginRequiredException -> Result.failure(IncorrectDataException())
            is FirebaseAuthInvalidCredentialsException -> Result.failure(IncorrectDataException())
            is FirebaseAuthException -> Result.failure(NetworkException())
            is NullPointerException -> Result.failure(throwable)
            else -> Result.failure(Exception(throwable.message))
        }
    }
}