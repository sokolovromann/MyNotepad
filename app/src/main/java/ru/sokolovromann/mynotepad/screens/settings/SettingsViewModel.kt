package ru.sokolovromann.mynotepad.screens.settings

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
import ru.sokolovromann.mynotepad.data.local.account.Account
import ru.sokolovromann.mynotepad.data.repository.AccountRepository
import ru.sokolovromann.mynotepad.data.repository.SettingsRepository
import ru.sokolovromann.mynotepad.screens.ScreensEvent
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository,
    private val accountRepository: AccountRepository
) : ViewModel(), ScreensEvent<SettingsEvent> {

    private val _settingsState: MutableState<SettingsState> = mutableStateOf(SettingsState.Empty)
    val settingsState: State<SettingsState> = _settingsState

    private val _accountState: MutableState<Account> = mutableStateOf(Account.LocalAccount)
    val accountState: State<Account> = _accountState

    private val _settingsUiEvent: MutableSharedFlow<SettingsUiEvent> = MutableSharedFlow()
    val settingsUiEvent: SharedFlow<SettingsUiEvent> = _settingsUiEvent

    init {
        getSettings()
        getAccount()
    }

    override fun onEvent(event: SettingsEvent) {
        when (event) {
            is SettingsEvent.OnAppNightThemeChange -> saveAppNightTheme(event.appNightTheme)

            SettingsEvent.GitHubClick -> viewModelScope.launch {
                _settingsUiEvent.emit(SettingsUiEvent.OpenGitHub)
            }

            is SettingsEvent.OnNavigationMenuStateChange -> viewModelScope.launch {
                if (event.isOpen) {
                    _settingsUiEvent.emit(SettingsUiEvent.OpenNavigationMenu)
                } else {
                    _settingsUiEvent.emit(SettingsUiEvent.CloseNavigationMenu)
                }
            }

            SettingsEvent.SignUpClick -> viewModelScope.launch {
                _settingsUiEvent.emit(SettingsUiEvent.OpenSignUp)
            }

            SettingsEvent.SignInClick -> viewModelScope.launch {
                _settingsUiEvent.emit(SettingsUiEvent.OpenSignIn)
            }

            SettingsEvent.ChangeEmailClick -> viewModelScope.launch {
                _settingsUiEvent.emit(SettingsUiEvent.OpenChangeEmail)
            }

            SettingsEvent.ChangePasswordClick -> viewModelScope.launch {
                _settingsUiEvent.emit(SettingsUiEvent.OpenChangePassword)
            }

            SettingsEvent.SignOutClick -> signOut()

            SettingsEvent.DeleteAccountClick -> viewModelScope.launch {
                _settingsUiEvent.emit(SettingsUiEvent.OpenDeleteAccount)
            }
        }
    }

    private fun getSettings() {
        _settingsState.value = SettingsState.Loading

        viewModelScope.launch(Dispatchers.IO) {
            settingsRepository.getSettings().collect { settings ->
                withContext(Dispatchers.Main) {
                    _settingsState.value = SettingsState.SettingsDisplay(settings)
                }
            }
        }
    }

    private fun getAccount() {
        viewModelScope.launch(Dispatchers.IO) {
            accountRepository.getAccount().collect { account ->
                withContext(Dispatchers.Main) {
                    _accountState.value = account
                }
            }
        }
    }

    private fun saveAppNightTheme(appNightTheme: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            settingsRepository.saveAppNightTheme(appNightTheme)
        }
    }

    private fun signOut() {
        viewModelScope.launch(Dispatchers.IO) {
            accountRepository.signOut {
                viewModelScope.launch {
                    _settingsUiEvent.emit(SettingsUiEvent.OpenWelcome)
                }
            }
        }
    }
}