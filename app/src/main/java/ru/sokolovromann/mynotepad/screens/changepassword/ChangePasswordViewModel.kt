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
import javax.inject.Inject

@HiltViewModel
class ChangePasswordViewModel @Inject constructor(
    private val accountRepository: AccountRepository
) : ViewModel(), ScreensEvent<ChangePasswordEvent> {

    private val _changePasswordState: MutableState<ChangePasswordState> = mutableStateOf(ChangePasswordState())
    val changePasswordState: State<ChangePasswordState> = _changePasswordState

    private val _changePasswordUiEvent: MutableSharedFlow<ChangePasswordUiEvent> = MutableSharedFlow()
    val changePasswordUiEvent: SharedFlow<ChangePasswordUiEvent> = _changePasswordUiEvent

    override fun onEvent(event: ChangePasswordEvent) {
        when (event) {
            is ChangePasswordEvent.OnNewPasswordChange -> _changePasswordState.value = _changePasswordState.value.copy(
                newPassword = event.newPassword
            )

            is ChangePasswordEvent.OnCurrentPasswordChange -> _changePasswordState.value = _changePasswordState.value.copy(
                currentPassword = event.newPassword
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
        val correctPassword = _changePasswordState.value.newPassword.length > 8
        _changePasswordState.value = _changePasswordState.value.copy(
            incorrectNewPassword = !correctPassword,
            changing = false
        )

        return !_changePasswordState.value.incorrectNewPassword
    }

    private fun isCorrectCurrentPassword(): Boolean {
        val correctPassword = _changePasswordState.value.currentPassword.isNotEmpty()
        _changePasswordState.value = _changePasswordState.value.copy(
            incorrectCurrentPassword = !correctPassword,
            changing = false
        )

        return !_changePasswordState.value.incorrectCurrentPassword
    }

    private fun changePassword() {
        _changePasswordState.value = _changePasswordState.value.copy(
            changing = true
        )

        accountRepository.updatePassword(
            currentPassword = _changePasswordState.value.currentPassword,
            newPassword = _changePasswordState.value.newPassword
        ) { result ->
            _changePasswordState.value = _changePasswordState.value.copy(
                changing = false
            )

            viewModelScope.launch {
                result
                    .onSuccess {
                        _changePasswordUiEvent.emit(ChangePasswordUiEvent.ShowPasswordChangedMessage)
                    }
                    .onFailure { exception ->
                        when (exception) {
                            is NetworkException -> _changePasswordUiEvent.emit(
                                ChangePasswordUiEvent.ShowNetworkErrorMessage
                            )
                            is AuthException -> _changePasswordState.value = _changePasswordState.value.copy(
                                incorrectCurrentPassword = true
                            )
                            else -> _changePasswordUiEvent.emit(
                                ChangePasswordUiEvent.ShowUnknownErrorMessage
                            )
                        }
                    }
            }
        }
    }
}