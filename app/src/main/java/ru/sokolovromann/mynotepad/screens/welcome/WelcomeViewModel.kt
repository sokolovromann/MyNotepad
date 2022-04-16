package ru.sokolovromann.mynotepad.screens.welcome

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
import kotlinx.coroutines.withContext
import ru.sokolovromann.mynotepad.data.repository.AccountRepository
import ru.sokolovromann.mynotepad.screens.ScreensEvent
import javax.inject.Inject

@HiltViewModel
class WelcomeViewModel @Inject constructor(
    private val accountRepository: AccountRepository
) : ViewModel(), ScreensEvent<WelcomeEvent> {

    private val _loadingState: MutableState<Boolean> = mutableStateOf(false)
    val loadingState: State<Boolean> = _loadingState

    private val _welcomeUiEvent: MutableSharedFlow<WelcomeUiEvent> = MutableSharedFlow()
    val welcomeUiEvent: SharedFlow<WelcomeUiEvent> = _welcomeUiEvent

    init {
        getAccount()
    }

    override fun onEvent(event: WelcomeEvent) {
        when (event) {
            WelcomeEvent.SignUpClick -> viewModelScope.launch {
                _welcomeUiEvent.emit(WelcomeUiEvent.OpenSignUp)
            }

            WelcomeEvent.SignInClick -> viewModelScope.launch {
                _welcomeUiEvent.emit(WelcomeUiEvent.OpenSignIn)
            }

            WelcomeEvent.ContinueWithoutSyncClick -> saveLocalAccount()

            WelcomeEvent.CloseWelcome -> viewModelScope.launch {
                _welcomeUiEvent.emit(WelcomeUiEvent.OpenNotes)
            }
        }
    }

    private fun getAccount() {
        _loadingState.value = true

        viewModelScope.launch(Dispatchers.IO) {
            accountRepository.getAccount().collect { account ->
                withContext(Dispatchers.Main) {
                    if (account.isEmpty()) {
                        _loadingState.value = false
                    } else {
                        withContext(Dispatchers.IO) {
                            _welcomeUiEvent.emit(WelcomeUiEvent.OpenNotes)
                        }
                    }
                }
            }
        }
    }

    private fun saveLocalAccount() {
        viewModelScope.launch(Dispatchers.IO) {
            accountRepository.continueWithoutSignIn {}
        }
    }
}