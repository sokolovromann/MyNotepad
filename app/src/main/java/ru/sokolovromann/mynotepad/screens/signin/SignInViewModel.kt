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
import ru.sokolovromann.mynotepad.data.exception.IncorrectDataException
import ru.sokolovromann.mynotepad.data.exception.NetworkException
import ru.sokolovromann.mynotepad.data.local.note.NoteSyncState
import ru.sokolovromann.mynotepad.data.repository.AccountRepository
import ru.sokolovromann.mynotepad.data.repository.NoteRepository
import ru.sokolovromann.mynotepad.screens.ScreensEvent
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val accountRepository: AccountRepository,
    private val noteRepository: NoteRepository
) : ViewModel(), ScreensEvent<SignInEvent> {

    private val _signInState: MutableState<SignInState> = mutableStateOf(SignInState())
    val signInState: State<SignInState> = _signInState

    private val _signInUiEvent: MutableSharedFlow<SignInUiEvent> = MutableSharedFlow()
    val signInUiEvent: SharedFlow<SignInUiEvent> = _signInUiEvent

    override fun onEvent(event: SignInEvent) {
        when (event) {
            is SignInEvent.OnEmailChange -> _signInState.value = _signInState.value.copy(
                email = event.newEmail
            )
            is SignInEvent.OnPasswordChange -> _signInState.value = _signInState.value.copy(
                password = event.newPassword
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
        val correctEmail = Patterns.EMAIL_ADDRESS.matcher(_signInState.value.email).matches()
        _signInState.value = _signInState.value.copy(
            incorrectEmail = !correctEmail,
            signingIn = false
        )

        return !_signInState.value.incorrectEmail
    }

    private fun isCorrectPassword(): Boolean {
        val correctPassword = _signInState.value.password.isNotEmpty()
        _signInState.value = _signInState.value.copy(
            incorrectPassword = !correctPassword,
            signingIn = false
        )

        return !_signInState.value.incorrectPassword
    }

    private fun signIn() {
        _signInState.value = _signInState.value.copy(
            signingIn = true
        )

        viewModelScope.launch(Dispatchers.IO) {
            accountRepository.signInWithEmailPassword(
                email = _signInState.value.email,
                password = _signInState.value.password,
            ) { result ->
                viewModelScope.launch(Dispatchers.Main) {
                    _signInState.value = _signInState.value.copy(
                        signingIn = false
                    )

                    result
                        .onSuccess { account ->
                            prepareNotesForSync(account.uid)
                            _signInUiEvent.emit(SignInUiEvent.OpenNotes)
                        }
                        .onFailure { exception ->
                            when (exception) {
                                is NetworkException -> _signInUiEvent.emit(
                                    SignInUiEvent.ShowNetworkErrorMessage
                                )
                                is IncorrectDataException -> _signInUiEvent.emit(
                                    SignInUiEvent.ShowSignInErrorMessage
                                )
                                else -> _signInUiEvent.emit(
                                    SignInUiEvent.ShowUnknownErrorMessage
                                )
                            }
                        }
                }
            }
        }
    }

    private fun sendPasswordResetEmail() {
        viewModelScope.launch(Dispatchers.IO) {
            accountRepository.sendPasswordResetEmail(
                email = _signInState.value.email
            ) { result ->
                viewModelScope.launch(Dispatchers.Main) {
                    result
                        .onSuccess {
                            _signInUiEvent.emit(SignInUiEvent.ShowResetPasswordMessage)
                        }
                        .onFailure { exception ->
                            if (exception is NetworkException) {
                                _signInUiEvent.emit(SignInUiEvent.ShowNetworkErrorMessage)
                            } else {
                                _signInUiEvent.emit(SignInUiEvent.ShowUnknownErrorMessage)
                            }
                        }
                }
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