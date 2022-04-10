package ru.sokolovromann.mynotepad.screens.changeemail

import android.util.Patterns
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import ru.sokolovromann.mynotepad.data.exception.AuthException
import ru.sokolovromann.mynotepad.data.exception.NetworkException
import ru.sokolovromann.mynotepad.data.repository.AccountRepository
import ru.sokolovromann.mynotepad.screens.ScreensEvent
import ru.sokolovromann.mynotepad.screens.changeemail.state.ChangeEmailEmailFieldState
import ru.sokolovromann.mynotepad.screens.changeemail.state.ChangeEmailPasswordFieldState
import javax.inject.Inject

@HiltViewModel
class ChangeEmailViewModel @Inject constructor(
    private val accountRepository: AccountRepository
) : ViewModel(), ScreensEvent<ChangeEmailEvent> {

    private val _emailFieldState: MutableState<ChangeEmailEmailFieldState> = mutableStateOf(ChangeEmailEmailFieldState.Default)
    val emailFieldState: State<ChangeEmailEmailFieldState> = _emailFieldState

    private val _passwordFieldState: MutableState<ChangeEmailPasswordFieldState> = mutableStateOf(ChangeEmailPasswordFieldState.Default)
    val passwordFieldState: State<ChangeEmailPasswordFieldState> = _passwordFieldState

    private val _changingState: MutableState<Boolean> = mutableStateOf(false)
    val changingState: State<Boolean> = _changingState

    private val _changeEmailUiEvent: MutableSharedFlow<ChangeEmailUiEvent> = MutableSharedFlow()
    val changeEmailUiEvent: SharedFlow<ChangeEmailUiEvent> = _changeEmailUiEvent

    override fun onEvent(event: ChangeEmailEvent) {
        when (event) {
            is ChangeEmailEvent.OnEmailChange -> _emailFieldState.value = _emailFieldState.value.copy(
                email = event.newEmail.trim(),
                showError = event.newEmail.trim().isEmpty()
            )

            is ChangeEmailEvent.OnPasswordChange -> _passwordFieldState.value = _passwordFieldState.value.copy(
                password = event.newPassword,
                showError = event.newPassword.isEmpty()
            )

            ChangeEmailEvent.ChangeClick -> if (isCorrectEmail() && isCorrectPassword()) {
                changeEmail()
            }

            ChangeEmailEvent.CloseClick -> viewModelScope.launch {
                _changeEmailUiEvent.emit(ChangeEmailUiEvent.OpenSettings)
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

    private fun changeEmail() {
        _changingState.value = true

        accountRepository.updateEmail(
            currentPassword = _passwordFieldState.value.password,
            newEmail = _emailFieldState.value.email
        ) { result ->
            _changingState.value = false

            viewModelScope.launch {
                onResult(result)
            }
        }
    }

    private suspend fun onResult(updateEmailResult: Result<Unit>) {
        updateEmailResult
            .onSuccess { _changeEmailUiEvent.emit(ChangeEmailUiEvent.ShowEmailChangedMessage) }
            .onFailure { exception ->
                when (exception) {
                    is NetworkException -> _changeEmailUiEvent.emit(
                        ChangeEmailUiEvent.ShowNetworkErrorMessage
                    )

                    is AuthException -> _passwordFieldState.value = _passwordFieldState.value.copy(
                        showError = true
                    )

                    else -> _changeEmailUiEvent.emit(
                        ChangeEmailUiEvent.ShowUnknownErrorMessage
                    )
                }
            }
    }
}