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
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.sokolovromann.mynotepad.BuildConfig
import ru.sokolovromann.mynotepad.data.local.account.Account
import ru.sokolovromann.mynotepad.data.local.settings.NotesSyncPeriod
import ru.sokolovromann.mynotepad.data.repository.AccountRepository
import ru.sokolovromann.mynotepad.data.repository.NoteRepository
import ru.sokolovromann.mynotepad.data.repository.SettingsRepository
import ru.sokolovromann.mynotepad.screens.ScreensEvent
import ru.sokolovromann.mynotepad.screens.settings.state.SettingsSyncPeriodDialogState
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository,
    private val accountRepository: AccountRepository,
    private val noteRepository: NoteRepository
) : ViewModel(), ScreensEvent<SettingsEvent> {

    private val _appNightThemeState: MutableState<Boolean> = mutableStateOf(false)
    val appNightThemeState: State<Boolean> = _appNightThemeState

    private val _notesSaveAndCloseState: MutableState<Boolean> = mutableStateOf(false)
    val notesSaveAndCloseState: State<Boolean> = _notesSaveAndCloseState

    private val _localAccountState: MutableState<Boolean> = mutableStateOf(false)
    val localAccountState: State<Boolean> = _localAccountState

    private val _accountNameState: MutableState<String> = mutableStateOf(Account.DEFAULT_NAME)
    val accountNameState: State<String> = _accountNameState

    private val _notesSyncPeriodState: MutableState<NotesSyncPeriod> = mutableStateOf(NotesSyncPeriod.THREE_HOURS)
    val notesSyncPeriodState: State<NotesSyncPeriod> = _notesSyncPeriodState

    private val _appVersionState: MutableState<String> = mutableStateOf(BuildConfig.VERSION_NAME)
    val appVersionState: State<String> = _appVersionState

    private val _syncPeriodDialogState: MutableState<SettingsSyncPeriodDialogState> = mutableStateOf(SettingsSyncPeriodDialogState.Default)
    val syncPeriodDialogState: State<SettingsSyncPeriodDialogState> = _syncPeriodDialogState

    private val _settingsUiEvent: MutableSharedFlow<SettingsUiEvent> = MutableSharedFlow()
    val settingsUiEvent: SharedFlow<SettingsUiEvent> = _settingsUiEvent

    init {
        getSettings()
        getAccount()
    }

    override fun onEvent(event: SettingsEvent) {
        when (event) {
            is SettingsEvent.OnAppNightThemeChange -> saveAppNightTheme(event.appNightTheme)

            is SettingsEvent.OnNotesSaveAndCloseChange -> saveNotesSaveAndClose(event.notesSaveAndClose)

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

            SettingsEvent.FeedbackClick -> viewModelScope.launch {
                _settingsUiEvent.emit(SettingsUiEvent.OpenEmailApp)
            }

            SettingsEvent.TermsClick -> viewModelScope.launch {
                _settingsUiEvent.emit(SettingsUiEvent.OpenTerms)
            }

            SettingsEvent.PrivacyPolicyClick -> viewModelScope.launch {
                _settingsUiEvent.emit(SettingsUiEvent.OpenPrivacyPolicy)
            }

            SettingsEvent.BackClick -> viewModelScope.launch {
                _settingsUiEvent.emit(SettingsUiEvent.OpenNotes)
            }

            is SettingsEvent.OnSyncPeriodDialogChange -> _syncPeriodDialogState.value = _syncPeriodDialogState.value.copy(
                showDialog = event.show
            )

            is SettingsEvent.OnNotesSyncPeriodChange -> saveNotesSyncPeriod(event.notesSyncPeriod)
        }
    }

    private fun getSettings() {
        viewModelScope.launch(Dispatchers.IO) {
            settingsRepository.getSettings().collect { settings ->
                withContext(Dispatchers.Main) {
                    _appNightThemeState.value = settings.appNightTheme
                    _notesSaveAndCloseState.value = settings.notesSaveAndClose
                    _notesSyncPeriodState.value = settings.notesSyncPeriod
                    _syncPeriodDialogState.value = _syncPeriodDialogState.value.copy(
                        selected = settings.notesSyncPeriod
                    )
                }
            }
        }
    }

    private fun getAccount() {
        viewModelScope.launch(Dispatchers.IO) {
            accountRepository.getAccount().collect { account ->
                withContext(Dispatchers.Main) {
                    _localAccountState.value = account.isLocalAccount()
                    _accountNameState.value = account.getName()
                }
            }
        }
    }

    private fun saveAppNightTheme(appNightTheme: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            settingsRepository.saveAppNightTheme(appNightTheme)
        }
    }

    private fun saveNotesSaveAndClose(notesSaveAndClose: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            settingsRepository.saveNotesSaveAndClose(notesSaveAndClose)
        }
    }

    private fun saveNotesSyncPeriod(notesSyncPeriod: NotesSyncPeriod) {
        viewModelScope.launch(Dispatchers.IO) {
            settingsRepository.saveNotesSyncPeriod(notesSyncPeriod)
        }
    }

    private fun signOut() {
        viewModelScope.launch(Dispatchers.IO) {
            accountRepository.signOut {
                clearNotes()
                clearSetting()
                viewModelScope.launch {
                    _settingsUiEvent.emit(SettingsUiEvent.OpenWelcome)
                }
            }
        }
    }

    private fun clearNotes() {
        viewModelScope.launch(Dispatchers.IO) {
            noteRepository.clearNotes("", NoteRepository.LOCAL_TOKEN_ID)
        }
    }

    private fun clearSetting() {
        viewModelScope.launch(Dispatchers.IO) {
            settingsRepository.clearSettings()
        }
    }
}