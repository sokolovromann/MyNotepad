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
import ru.sokolovromann.mynotepad.data.exception.NetworkException
import ru.sokolovromann.mynotepad.data.repository.AccountRepository
import ru.sokolovromann.mynotepad.screens.ScreensEvent
import javax.inject.Inject

@HiltViewModel
class ChangeEmailViewModel @Inject constructor(
    private val accountRepository: AccountRepository
) : ViewModel(), ScreensEvent<ChangeEmailEvent> {

    private val _changeEmailState: MutableState<ChangeEmailState> = mutableStateOf(ChangeEmailState())
    val changeEmailState: State<ChangeEmailState> = _changeEmailState

    private val _changeEmailUiEvent: MutableSharedFlow<ChangeEmailUiEvent> = MutableSharedFlow()
    val changeEmailUiEvent: SharedFlow<ChangeEmailUiEvent> = _changeEmailUiEvent

    override fun onEvent(event: ChangeEmailEvent) {
        when (event) {
            is ChangeEmailEvent.OnEmailChange -> _changeEmailState.value = _changeEmailState.value.copy(
                email = event.newEmail
            )

            ChangeEmailEvent.ChangeClick -> if (isCorrectEmail()) {
                changeEmail()
            }

            ChangeEmailEvent.CloseClick -> viewModelScope.launch {
                _changeEmailUiEvent.emit(ChangeEmailUiEvent.OpenSettings)
            }
        }
    }

    private fun isCorrectEmail(): Boolean {
        val correctEmail = Patterns.EMAIL_ADDRESS.matcher(_changeEmailState.value.email).matches()
        _changeEmailState.value = _changeEmailState.value.copy(
            incorrectEmail = !correctEmail,
            changing = false
        )

        return !_changeEmailState.value.incorrectEmail
    }

    private fun changeEmail() {
        _changeEmailState.value = _changeEmailState.value.copy(
            changing = true
        )

        accountRepository.updateEmail(email = _changeEmailState.value.email) { result ->
            _changeEmailState.value = _changeEmailState.value.copy(
                changing = false
            )

            viewModelScope.launch {
                result
                    .onSuccess {
                        _changeEmailUiEvent.emit(ChangeEmailUiEvent.ShowEmailChangedMessage)
                    }
                    .onFailure { exception ->
                        when (exception) {
                            is NetworkException -> _changeEmailUiEvent.emit(
                                ChangeEmailUiEvent.ShowNetworkErrorMessage
                            )
                            else -> _changeEmailUiEvent.emit(
                                ChangeEmailUiEvent.ShowUnknownErrorMessage
                            )
                        }
                    }
            }
        }
    }
}