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
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.sokolovromann.mynotepad.data.repository.AccountRepository
import ru.sokolovromann.mynotepad.screens.ScreensEvent
import javax.inject.Inject

@HiltViewModel
class WelcomeViewModel @Inject constructor(
    private val accountRepository: AccountRepository
) : ViewModel(), ScreensEvent<WelcomeEvent> {

    private val _welcomeState: MutableState<WelcomeState> = mutableStateOf(WelcomeState.Empty)
    val welcomeState: State<WelcomeState> = _welcomeState

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
        _welcomeState.value = WelcomeState.Loading

        viewModelScope.launch(Dispatchers.IO) {
            accountRepository.getAccount().collect { account ->
                withContext(Dispatchers.Main) {
                    if (account.isEmpty()) {
                        _welcomeState.value = WelcomeState.Welcome
                    } else {
                        _welcomeState.value = WelcomeState.Empty
                        withContext(Dispatchers.IO) {
                            onEvent(WelcomeEvent.CloseWelcome)
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