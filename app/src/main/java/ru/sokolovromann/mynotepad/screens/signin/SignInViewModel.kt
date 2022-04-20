package ru.sokolovromann.mynotepad.screens.signin

import android.util.Patterns
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import ru.sokolovromann.mynotepad.data.exception.AuthException
import ru.sokolovromann.mynotepad.data.exception.NetworkException
import ru.sokolovromann.mynotepad.data.local.account.Account
import ru.sokolovromann.mynotepad.data.local.note.NoteSyncState
import ru.sokolovromann.mynotepad.data.repository.AccountRepository
import ru.sokolovromann.mynotepad.data.repository.NoteRepository
import ru.sokolovromann.mynotepad.screens.ScreensEvent
import ru.sokolovromann.mynotepad.screens.signin.state.SignInEmailFieldState
import ru.sokolovromann.mynotepad.screens.signin.state.SignInPasswordFieldState
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val accountRepository: AccountRepository,
    private val noteRepository: NoteRepository
) : ViewModel(), ScreensEvent<SignInEvent> {

    private val _emailFieldState: MutableState<SignInEmailFieldState> = mutableStateOf(SignInEmailFieldState.Default)
    val emailFieldState: State<SignInEmailFieldState> = _emailFieldState

    private val _passwordFieldState: MutableState<SignInPasswordFieldState> = mutableStateOf(SignInPasswordFieldState.Default)
    val passwordFieldState: State<SignInPasswordFieldState> = _passwordFieldState

    private val _signingIn: MutableState<Boolean> = mutableStateOf(false)
    val signingIn: State<Boolean> = _signingIn

    private val _signInUiEvent: MutableSharedFlow<SignInUiEvent> = MutableSharedFlow()
    val signInUiEvent: SharedFlow<SignInUiEvent> = _signInUiEvent

    override fun onEvent(event: SignInEvent) {
        when (event) {
            is SignInEvent.OnEmailChange -> _emailFieldState.value = _emailFieldState.value.copy(
                email = event.newEmail.trim(),
                showError = event.newEmail.trim().isEmpty()
            )

            is SignInEvent.OnPasswordChange -> _passwordFieldState.value = _passwordFieldState.value.copy(
                password = event.newPassword,
                showError = event.newPassword.isEmpty()
            )

            SignInEvent.SignInClick -> if (isCorrectEmail() && isCorrectPassword()) {
                signIn()
            }

            SignInEvent.ResetPasswordClick -> if (isCorrectEmail()) {
                sendPasswordResetEmail()
            }

            SignInEvent.CloseClick -> viewModelScope.launch {
                _signInUiEvent.emit(SignInUiEvent.OpenWelcome)
            }
        }
    }

    private fun isCorrectEmail(): Boolean {
        val correctEmail = Patterns.EMAIL_ADDRESS.matcher(_emailFieldState.value.email).matches()
        _emailFieldState.value = _emailFieldState.value.copy(
            showError = !correctEmail
        )

        return correctEmail
    }

    private fun isCorrectPassword(): Boolean {
        val correctPassword = _passwordFieldState.value.password.isNotEmpty()
        _passwordFieldState.value = _passwordFieldState.value.copy(
            showError = !correctPassword
        )

        return correctPassword
    }

    private fun signIn() {
        _signingIn.value = true

        viewModelScope.launch(Dispatchers.IO) {
            accountRepository.signInWithEmailPassword(
                email = _emailFieldState.value.email,
                password = _passwordFieldState.value.password,
            ) { result ->
                _signingIn.value = false

                viewModelScope.launch {
                    onSignInResult(result)
                }
            }
        }
    }

    private fun sendPasswordResetEmail() {
        viewModelScope.launch(Dispatchers.IO) {
            accountRepository.sendPasswordResetEmail(
                email = _emailFieldState.value.email
            ) { result ->
                viewModelScope.launch {
                    onSendPasswordResetEmail(result)
                }
            }
        }
    }

    private suspend fun onSignInResult(signInResult: Result<Account>) {
        signInResult
            .onSuccess { account ->
                prepareNotesForSync(account.uid)
                _signInUiEvent.emit(SignInUiEvent.OpenNotes)
            }
            .onFailure { exception ->
                when (exception) {
                    is NetworkException -> _signInUiEvent.emit(
                        SignInUiEvent.ShowNetworkErrorMessage
                    )
                    is AuthException -> _signInUiEvent.emit(
                        SignInUiEvent.ShowSignInErrorMessage
                    )
                    else -> _signInUiEvent.emit(
                        SignInUiEvent.ShowUnknownErrorMessage
                    )
                }
            }
    }

    private suspend fun onSendPasswordResetEmail(sendPasswordResetEmailResult: Result<Unit>) {
        sendPasswordResetEmailResult
            .onSuccess { _signInUiEvent.emit(SignInUiEvent.ShowResetPasswordMessage) }
            .onFailure { exception ->
                if (exception is NetworkException) {
                    _signInUiEvent.emit(SignInUiEvent.ShowNetworkErrorMessage)
                } else {
                    _signInUiEvent.emit(SignInUiEvent.ShowUnknownErrorMessage)
                }
            }
    }

    private fun prepareNotesForSync(owner: String) {
        viewModelScope.launch(Dispatchers.IO) {
            noteRepository.prepareNotesForSync(
                syncState = NoteSyncState.SAVE,
                owner = owner
            )
        }
    }
}