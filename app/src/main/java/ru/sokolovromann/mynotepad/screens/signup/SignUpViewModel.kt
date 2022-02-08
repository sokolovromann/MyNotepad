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
import ru.sokolovromann.mynotepad.data.local.note.NoteSyncState
import ru.sokolovromann.mynotepad.data.repository.AccountRepository
import ru.sokolovromann.mynotepad.data.repository.NoteRepository
import ru.sokolovromann.mynotepad.screens.ScreensEvent
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val accountRepository: AccountRepository,
    private val noteRepository: NoteRepository
) : ViewModel(), ScreensEvent<SignUpEvent> {

    private val _signUpState: MutableState<SignUpState> = mutableStateOf(SignUpState())
    val signUpState: State<SignUpState> = _signUpState

    private val _signUpUiEvent: MutableSharedFlow<SignUpUiEvent> = MutableSharedFlow()
    val signUpUiEvent: SharedFlow<SignUpUiEvent> = _signUpUiEvent

    override fun onEvent(event: SignUpEvent) {
        when (event) {
            is SignUpEvent.OnEmailChange -> _signUpState.value = _signUpState.value.copy(
                email = event.newEmail
            )
            is SignUpEvent.OnPasswordChange -> _signUpState.value = _signUpState.value.copy(
                password = event.newPassword
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
        val correctEmail = Patterns.EMAIL_ADDRESS.matcher(_signUpState.value.email).matches()
        _signUpState.value = _signUpState.value.copy(
            incorrectEmail = !correctEmail,
            creatingAccount = false
        )

        val correctMinLengthPassword = _signUpState.value.password.length >= 8
        _signUpState.value = _signUpState.value.copy(
            incorrectMinLengthPassword = !correctMinLengthPassword,
            creatingAccount = false
        )

        return !_signUpState.value.incorrectEmail &&
                !_signUpState.value.incorrectMinLengthPassword
    }

    private fun createAccount() {
        _signUpState.value = _signUpState.value.copy(
            creatingAccount = true
        )

        viewModelScope.launch(Dispatchers.IO) {
            accountRepository.signUpWithEmailPassword(
                email = _signUpState.value.email,
                password = _signUpState.value.password,
            ) { result ->
                viewModelScope.launch(Dispatchers.Main) {
                    _signUpState.value = _signUpState.value.copy(
                        creatingAccount = false
                    )

                    result
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