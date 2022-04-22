package ru.sokolovromann.mynotepad.screens.settings

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import ru.sokolovromann.mynotepad.MyNotepadRoute
import ru.sokolovromann.mynotepad.R
import ru.sokolovromann.mynotepad.screens.settings.components.*
import ru.sokolovromann.mynotepad.ui.components.NavigationDrawer
import ru.sokolovromann.mynotepad.ui.components.NavigationDrawerHeader

@ExperimentalFoundationApi
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel(),
    navController: NavController,
    onOpenGitHub: () -> Unit,
    onOpenEmailApp: () -> Unit,
    onOpenTerms: () -> Unit,
    onOpenPrivacyPolicy: () -> Unit
) {
    val scrollState = rememberScrollState()
    val scaffoldState = rememberScaffoldState()

    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewModel.settingsUiEvent.collect { uiEvent ->
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

                SettingsUiEvent.OpenNotes -> {
                    navController.apply {
                        previousBackStackEntry?.arguments?.clear()
                        popBackStack()
                    }
                }
            }
        }
    }

    BackHandler(enabled = scaffoldState.drawerState.isOpen) {
        viewModel.onEvent(SettingsEvent.OnNavigationMenuStateChange(false))
    }

    BackHandler {
        viewModel.onEvent(SettingsEvent.BackClick)
    }

    Scaffold(
        topBar = {
            SettingsTopAppBar(onNavigationIconClick = {
                viewModel.onEvent(SettingsEvent.OnNavigationMenuStateChange(true))
            })
        },
        scaffoldState = scaffoldState,
        drawerContent = {
            NavigationDrawer(
                navController = navController,
                closeNavigation = {
                    viewModel.onEvent(
                        SettingsEvent.OnNavigationMenuStateChange(false)
                    )},
                drawerHeader = {
                    NavigationDrawerHeader(
                        title = stringResource(id = R.string.app_name),
                        description = viewModel.accountNameState.value
                    )
                }
            )
        },
        snackbarHost = { scaffoldState.snackbarHostState }
    ) {
        Column(modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.surface)
            .verticalScroll(scrollState)
        ) {
            SettingsGeneralContent(
                appNightTheme = viewModel.appNightThemeState.value,
                notesSaveAndClose = viewModel.notesSaveAndCloseState.value,
                onAppNightThemeChange = { viewModel.onEvent(SettingsEvent.OnAppNightThemeChange(it)) },
                onNotesSaveAndClose = { viewModel.onEvent(SettingsEvent.OnNotesSaveAndCloseChange(it)) }
            )
            Divider()
            SettingsAccountContent(
                localAccount = viewModel.localAccountState.value,
                onSignUpClick = { viewModel.onEvent(SettingsEvent.SignUpClick) },
                onSignInClick = { viewModel.onEvent(SettingsEvent.SignInClick) },
                accountName = viewModel.accountNameState.value,
                notesSyncPeriod = viewModel.notesSyncPeriodState.value,
                onSyncPeriodClick = { viewModel.onEvent(SettingsEvent.OnSyncPeriodDialogChange(true)) },
                onChangeEmailClick = { viewModel.onEvent(SettingsEvent.ChangeEmailClick) },
                onChangePasswordClick = { viewModel.onEvent(SettingsEvent.ChangePasswordClick) },
                onSignOutClick = { viewModel.onEvent(SettingsEvent.SignOutClick) },
                onDeleteAccountClick = { viewModel.onEvent(SettingsEvent.DeleteAccountClick) }
            )
            Divider()
            SettingsAboutContent(
                appVersion = viewModel.appVersionState.value,
                onGitHubClick = { viewModel.onEvent(SettingsEvent.GitHubClick) },
                onFeedbackClick = { viewModel.onEvent(SettingsEvent.FeedbackClick) },
                onTermsClick = { viewModel.onEvent(SettingsEvent.TermsClick) },
                onPrivacyPolicyClick = { viewModel.onEvent(SettingsEvent.PrivacyPolicyClick) }
            )
        }

        SettingsSyncPeriodDialog(
            dialogState = viewModel.syncPeriodDialogState.value,
            onDismiss = { viewModel.onEvent(SettingsEvent.OnSyncPeriodDialogChange(false)) },
            onSelectedChange = {
                viewModel.onEvent(SettingsEvent.OnNotesSyncPeriodChange(it))
                viewModel.onEvent(SettingsEvent.OnSyncPeriodDialogChange(false))
            }
        )
    }
}