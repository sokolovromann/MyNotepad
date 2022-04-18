package ru.sokolovromann.mynotepad.screens.signup

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
import ru.sokolovromann.mynotepad.data.exception.NetworkException
import ru.sokolovromann.mynotepad.data.local.account.Account
import ru.sokolovromann.mynotepad.data.local.note.NoteSyncState
import ru.sokolovromann.mynotepad.data.repository.AccountRepository
import ru.sokolovromann.mynotepad.data.repository.NoteRepository
import ru.sokolovromann.mynotepad.screens.ScreensEvent
import ru.sokolovromann.mynotepad.screens.signup.state.SignUpEmailFieldState
import ru.sokolovromann.mynotepad.screens.signup.state.SignUpPasswordFieldState
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val accountRepository: AccountRepository,
    private val noteRepository: NoteRepository
) : ViewModel(), ScreensEvent<SignUpEvent> {

    private val _emailFieldState: MutableState<SignUpEmailFieldState> = mutableStateOf(SignUpEmailFieldState.Default)
    val emailFieldState: State<SignUpEmailFieldState> = _emailFieldState

    private val _passwordFieldState: MutableState<SignUpPasswordFieldState> = mutableStateOf(SignUpPasswordFieldState.Default)
    val passwordFieldState: State<SignUpPasswordFieldState> = _passwordFieldState

    private val _creatingState: MutableState<Boolean> = mutableStateOf(false)
    val creatingState: State<Boolean> = _creatingState

    private val _signUpUiEvent: MutableSharedFlow<SignUpUiEvent> = MutableSharedFlow()
    val signUpUiEvent: SharedFlow<SignUpUiEvent> = _signUpUiEvent

    override fun onEvent(event: SignUpEvent) {
        when (event) {
            is SignUpEvent.OnEmailChange -> _emailFieldState.value = _emailFieldState.value.copy(
                email = event.newEmail.trim(),
                showError = event.newEmail.trim().isEmpty()
            )

            is SignUpEvent.OnPasswordChange -> _passwordFieldState.value = _passwordFieldState.value.copy(
                password = event.newPassword,
                showError = event.newPassword.isEmpty()
            )

            SignUpEvent.CreateAccountClick -> if (isCorrectEmailPassword()) {
                createAccount()
            }

            SignUpEvent.CloseClick -> viewModelScope.launch {
                _signUpUiEvent.emit(SignUpUiEvent.OpenWelcome)
            }
        }
    }

    private fun isCorrectEmailPassword(): Boolean {
        val correctEmail = Patterns.EMAIL_ADDRESS.matcher(_emailFieldState.value.email).matches()
        _emailFieldState.value = _emailFieldState.value.copy(
            showError = !correctEmail
        )

        val correctMinLengthPassword = _passwordFieldState.value.password.length >= 8
        _passwordFieldState.value = _passwordFieldState.value.copy(
            showError = !correctMinLengthPassword
        )

        return correctEmail && correctMinLengthPassword
    }

    private fun createAccount() {
        _creatingState.value = true

        viewModelScope.launch(Dispatchers.IO) {
            accountRepository.signUpWithEmailPassword(
                email = _emailFieldState.value.email,
                password = _passwordFieldState.value.password,
            ) { result ->
                _creatingState.value = false

                viewModelScope.launch {
                    onResult(result)
                }
            }
        }
    }

    private suspend fun onResult(signUpResult: Result<Account>) {
        signUpResult
            .onSuccess { account ->
                prepareNotesForSync(account.uid)
                _signUpUiEvent.emit(SignUpUiEvent.OpenNotes)
            }
            .onFailure { exception ->
                if (exception is NetworkException) {
                    _signUpUiEvent.emit(SignUpUiEvent.ShowNetworkErrorMessage)
                } else {
                    _signUpUiEvent.emit(SignUpUiEvent.ShowUnknownErrorMessage)
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