package ru.sokolovromann.mynotepad.screens.settings

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import ru.sokolovromann.mynotepad.BuildConfig
import ru.sokolovromann.mynotepad.MyNotepadRoute
import ru.sokolovromann.mynotepad.R
import ru.sokolovromann.mynotepad.screens.settings.components.SettingsDisplay
import ru.sokolovromann.mynotepad.screens.settings.components.SettingsLoading
import ru.sokolovromann.mynotepad.screens.settings.components.SettingsTopAppBar
import ru.sokolovromann.mynotepad.ui.components.NavigationDrawer
import ru.sokolovromann.mynotepad.ui.components.NavigationDrawerHeader

@ExperimentalFoundationApi
@Composable
fun SettingsScreen(
    settingsViewModel: SettingsViewModel = hiltViewModel(),
    navController: NavController,
    onOpenGitHub: () -> Unit,
    onOpenEmailApp: () -> Unit,
    onOpenTerms: () -> Unit,
    onOpenPrivacyPolicy: () -> Unit
) {
    val settingsState = settingsViewModel.settingsState.value
    val accountState = settingsViewModel.accountState
    val scaffoldState = rememberScaffoldState()

    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(true) {
        settingsViewModel.settingsUiEvent.collect { uiEvent ->
            when (uiEvent) {
                SettingsUiEvent.OpenGitHub -> onOpenGitHub()

                SettingsUiEvent.OpenNavigationMenu -> coroutineScope.launch {
                    scaffoldState.drawerState.open()
                }

                SettingsUiEvent.CloseNavigationMenu -> coroutineScope.launch {
                    scaffoldState.drawerState.close()
                }

                SettingsUiEvent.OpenSignUp -> navController.navigate(MyNotepadRoute.Welcome.signUpScreen)

                SettingsUiEvent.OpenSignIn -> navController.navigate(MyNotepadRoute.Welcome.signInScreen)

                SettingsUiEvent.OpenChangeEmail -> navController.navigate(MyNotepadRoute.Settings.changeEmailScreen)

                SettingsUiEvent.OpenChangePassword -> navController.navigate(MyNotepadRoute.Settings.changePasswordScreen)

                SettingsUiEvent.OpenWelcome -> navController.navigate(MyNotepadRoute.Welcome.welcomeScreen) {
                    navController.backQueue.clear()
                }

                SettingsUiEvent.OpenDeleteAccount -> navController.navigate(MyNotepadRoute.Settings.deleteAccountScreen)

                SettingsUiEvent.OpenEmailApp -> onOpenEmailApp()

                SettingsUiEvent.OpenTerms -> onOpenTerms()

                SettingsUiEvent.OpenPrivacyPolicy -> onOpenPrivacyPolicy()
            }
        }
    }

    BackHandler(enabled = scaffoldState.drawerState.isOpen) {
        settingsViewModel.onEvent(SettingsEvent.OnNavigationMenuStateChange(false))
    }

    Scaffold(
        topBar = {
            SettingsTopAppBar(onNavigationIconClick = {
                settingsViewModel.onEvent(SettingsEvent.OnNavigationMenuStateChange(true))
            })
        },
        scaffoldState = scaffoldState,
        drawerContent = {
            NavigationDrawer(
                navController = navController,
                closeNavigation = {
                    settingsViewModel.onEvent(
                        SettingsEvent.OnNavigationMenuStateChange(false)
                    )},
                drawerHeader = {
                    NavigationDrawerHeader(
                        title = stringResource(id = R.string.app_name),
                        description = accountState.value.getName()
                    )
                }
            )
        },
        snackbarHost = { scaffoldState.snackbarHostState }
    ) {
        if (settingsState.loading) {
            SettingsLoading()
        } else {
            SettingsDisplay(
                appNightTheme = settingsState.settings.appNightTheme,
                notesSaveAndClose = settingsState.settings.notesSaveAndClose,
                appVersion = BuildConfig.VERSION_NAME,
                onAppNightThemeChange = { appNightTheme ->
                    settingsViewModel.onEvent(SettingsEvent.OnAppNightThemeChange(appNightTheme))
                },
                onNotesSaveAndClose = { notesSaveAndClose ->
                    settingsViewModel.onEvent(SettingsEvent.OnNotesSaveAndCloseChange(notesSaveAndClose))
                },
                onGitHubClick = { settingsViewModel.onEvent(SettingsEvent.GitHubClick) },
                localAccount = accountState.value.isLocalAccount(),
                onSignUpClick = { settingsViewModel.onEvent(SettingsEvent.SignUpClick) },
                onSignInClick = { settingsViewModel.onEvent(SettingsEvent.SignInClick) },
                accountName = accountState.value.getName(),
                onChangeEmailClick = { settingsViewModel.onEvent(SettingsEvent.ChangeEmailClick) },
                onChangePasswordClick = { settingsViewModel.onEvent(SettingsEvent.ChangePasswordClick) },
                onSignOutClick = { settingsViewModel.onEvent(SettingsEvent.SignOutClick) },
                onDeleteAccountClick = { settingsViewModel.onEvent(SettingsEvent.DeleteAccountClick) },
                onFeedbackClick = { settingsViewModel.onEvent(SettingsEvent.FeedbackClick) },
                onTermsClick = { settingsViewModel.onEvent(SettingsEvent.TermsClick) },
                onPrivacyPolicyClick = { settingsViewModel.onEvent(SettingsEvent.PrivacyPolicyClick) }
            )
        }
    }
}