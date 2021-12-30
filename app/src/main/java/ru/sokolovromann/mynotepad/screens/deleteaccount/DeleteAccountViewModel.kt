package ru.sokolovromann.mynotepad.screens.deleteaccount

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import ru.sokolovromann.mynotepad.data.exception.IncorrectDataException
import ru.sokolovromann.mynotepad.data.exception.NetworkException
import ru.sokolovromann.mynotepad.data.repository.AccountRepository
import ru.sokolovromann.mynotepad.screens.ScreensEvent
import javax.inject.Inject

@HiltViewModel
class DeleteAccountViewModel @Inject constructor(
    private val accountRepository: AccountRepository
) : ViewModel(), ScreensEvent<DeleteAccountEvent> {

    private val _deleteAccountState: MutableState<DeleteAccountState> = mutableStateOf(DeleteAccountState())
    val deleteAccountState: State<DeleteAccountState> = _deleteAccountState

    private val _deleteAccountUiEvent: MutableSharedFlow<DeleteAccountUiEvent> = MutableSharedFlow()
    val deleteAccountUiEvent: SharedFlow<DeleteAccountUiEvent> = _deleteAccountUiEvent

    override fun onEvent(event: DeleteAccountEvent) {
        when (event) {
            is DeleteAccountEvent.OnPasswordChange -> _deleteAccountState.value = _deleteAccountState.value.copy(
                password = event.newPassword
            )

            DeleteAccountEvent.DeleteClick -> if (isCorrectPassword()) {
                deleteAccount()
            }

            DeleteAccountEvent.CloseClick -> viewModelScope.launch {
                _deleteAccountUiEvent.emit(DeleteAccountUiEvent.OpenSettings)
            }
        }
    }

    private fun isCorrectPassword(): Boolean {
        val correctPassword = _deleteAccountState.value.password.isNotEmpty()
        _deleteAccountState.value = _deleteAccountState.value.copy(
            incorrectPassword = !correctPassword,
            deleting = false
        )

        return !_deleteAccountState.value.incorrectPassword
    }

    private fun deleteAccount() {
        _deleteAccountState.value = _deleteAccountState.value.copy(
            deleting = true
        )

        accountRepository.deleteAccount(
            currentPassword = _deleteAccountState.value.password
        ) { result ->
            _deleteAccountState.value = _deleteAccountState.value.copy(
                deleting = false
            )

            viewModelScope.launch {
                result
                    .onSuccess {
                        _deleteAccountUiEvent.emit(DeleteAccountUiEvent.OpenWelcome)
                    }
                    .onFailure { exception ->
                        when (exception) {
                            is NetworkException -> _deleteAccountUiEvent.emit(
                                DeleteAccountUiEvent.ShowNetworkErrorMessage
                            )
                            is IncorrectDataException -> _deleteAccountState.value = _deleteAccountState.value.copy(
                                incorrectPassword = true
                            )
                            else -> _deleteAccountUiEvent.emit(
                                DeleteAccountUiEvent.ShowUnknownErrorMessage
                            )
                        }
                    }
            }
        }
    }
}