package ru.sokolovromann.mynotepad.data.repository

import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import ru.sokolovromann.mynotepad.data.exception.IncorrectDataException
import ru.sokolovromann.mynotepad.data.exception.NetworkException
import ru.sokolovromann.mynotepad.data.local.account.Account
import ru.sokolovromann.mynotepad.data.local.account.AccountDataStore
import ru.sokolovromann.mynotepad.data.remote.auth.AuthApi
import ru.sokolovromann.mynotepad.data.remote.auth.UserResponse

class AccountRepositoryImpl(
    private val dataStore: AccountDataStore,
    private val api: AuthApi,
    private val dispatcher: CoroutineDispatcher
) : AccountRepository {

    override suspend fun getAccount(): Flow<Account> {
        return dataStore.getAccount()
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
        onResult: (result: Result<Unit>) -> Unit
    ) {
        api.createUserWithEmailPassword(email, password) { userResult ->
            userResult
                .onSuccess { userResponse ->
                    cacheAccount(userResponse) { onResult(Result.success(Unit)) }
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
        onResult: (result: Result<Unit>) -> Unit
    ) {
        api.signInWithEmailPassword(email, password) { userResult ->
            userResult
                .onSuccess { userResponse ->
                    cacheAccount(userResponse) { onResult(Result.success(Unit)) }
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

    override fun updateEmail(email: String, onResult: (result: Result<Unit>) -> Unit) {
        api.updateEmail(email) { userResult ->
            userResult
                .onSuccess {
                    cacheEmail(email) { onResult(Result.success(Unit)) }
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

    override fun updatePassword(password: String, onResult: (result: Result<Unit>) -> Unit) {
        api.updatePassword(password) { userResult ->
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

    override fun deleteAccount(onResult: (result: Result<Unit>) -> Unit) {
        api.deleteCurrentUser { userResult ->
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

    private fun cacheAccount(userResponse: UserResponse, onCached: (account: Account) -> Unit) {
        runBlocking(dispatcher) {
            val account = Account(
                uid = userResponse.uid,
                email = userResponse.email,
                tokenId = userResponse.tokenId
            )
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
}