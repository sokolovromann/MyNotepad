package ru.sokolovromann.mynotepad.screens.changepassword

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
import ru.sokolovromann.mynotepad.screens.changepassword.state.CurrentPasswordFieldState
import ru.sokolovromann.mynotepad.screens.changepassword.state.NewPasswordFieldState
import javax.inject.Inject

@HiltViewModel
class ChangePasswordViewModel @Inject constructor(
    private val accountRepository: AccountRepository
) : ViewModel(), ScreensEvent<ChangePasswordEvent> {

    private val _newPasswordFieldState: MutableState<NewPasswordFieldState> = mutableStateOf(NewPasswordFieldState.Default)
    val newPasswordFieldState: State<NewPasswordFieldState> = _newPasswordFieldState

    private val _currentPasswordFieldState: MutableState<CurrentPasswordFieldState> = mutableStateOf(CurrentPasswordFieldState.Default)
    val currentPasswordFieldState: State<CurrentPasswordFieldState> = _currentPasswordFieldState

    private val _changingState: MutableState<Boolean> = mutableStateOf(false)
    val changingState: State<Boolean> = _changingState

    private val _changePasswordUiEvent: MutableSharedFlow<ChangePasswordUiEvent> = MutableSharedFlow()
    val changePasswordUiEvent: SharedFlow<ChangePasswordUiEvent> = _changePasswordUiEvent

    override fun onEvent(event: ChangePasswordEvent) {
        when (event) {
            is ChangePasswordEvent.OnNewPasswordChange -> _newPasswordFieldState.value = _newPasswordFieldState.value.copy(
                newPassword = event.newPassword,
                showError = event.newPassword.isEmpty()
            )

            is ChangePasswordEvent.OnCurrentPasswordChange -> _currentPasswordFieldState.value = _currentPasswordFieldState.value.copy(
                currentPassword = event.newPassword,
                showError = event.newPassword.isEmpty()
            )

            ChangePasswordEvent.ChangeClick -> if (isCorrectNewPassword() && isCorrectCurrentPassword()) {
                changePassword()
            }

            ChangePasswordEvent.CloseClick -> viewModelScope.launch {
                _changePasswordUiEvent.emit(ChangePasswordUiEvent.OpenSettings)
            }
        }
    }

    private fun isCorrectNewPassword(): Boolean {
        val correctPassword = _newPasswordFieldState.value.newPassword.length > 8
        _newPasswordFieldState.value = _newPasswordFieldState.value.copy(
            showError = !correctPassword
        )

        return correctPassword
    }

    private fun isCorrectCurrentPassword(): Boolean {
        val correctPassword = _currentPasswordFieldState.value.currentPassword.isNotEmpty()
        _currentPasswordFieldState.value = _currentPasswordFieldState.value.copy(
            showError = !correctPassword
        )

        return correctPassword
    }

    private fun changePassword() {
        _changingState.value = true

        accountRepository.updatePassword(
            currentPassword = _currentPasswordFieldState.value.currentPassword,
            newPassword = _newPasswordFieldState.value.newPassword
        ) { result ->
            _changingState.value = false

            viewModelScope.launch {
                onResult(result)
            }
        }
    }

    private suspend fun onResult(changePasswordResult: Result<Unit>) {
        changePasswordResult
            .onSuccess { _changePasswordUiEvent.emit(ChangePasswordUiEvent.ShowPasswordChangedMessage) }
            .onFailure { exception ->
                when (exception) {
                    is NetworkException -> _changePasswordUiEvent.emit(
                        ChangePasswordUiEvent.ShowNetworkErrorMessage
                    )

                    is AuthException -> _currentPasswordFieldState.value = _currentPasswordFieldState.value.copy(
                        showError = true
                    )

                    else -> _changePasswordUiEvent.emit(
                        ChangePasswordUiEvent.ShowUnknownErrorMessage)

                }
            }
    }
}