package ru.sokolovromann.mynotepad.screens.changeemail

import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.sokolovromann.mynotepad.R
import ru.sokolovromann.mynotepad.screens.changeemail.components.ChangeEmailDisplay
import ru.sokolovromann.mynotepad.screens.changeemail.components.ChangeEmailTopAppBar

@Composable
fun ChangeEmailScreen(
    changeEmailViewModel: ChangeEmailViewModel = hiltViewModel(),
    navController: NavController
) {
    val changeEmailState = changeEmailViewModel.changeEmailState
    val scaffoldState = rememberScaffoldState()

    val coroutineScope = rememberCoroutineScope()

    val changedMessage = stringResource(id = R.string.change_email_changed_message)
    val networkErrorMessage = stringResource(id = R.string.change_email_network_error_message)
    val unknownErrorMessage = stringResource(id = R.string.change_email_incorrect_email_message)

    LaunchedEffect(true) {
        changeEmailViewModel.changeEmailUiEvent.collectLatest { uiEvent ->
            when (uiEvent) {
                ChangeEmailUiEvent.OpenSettings -> navController.popBackStack()

                ChangeEmailUiEvent.ShowEmailChangedMessage -> coroutineScope.launch {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = changedMessage
                    )
                }

                ChangeEmailUiEvent.ShowNetworkErrorMessage -> coroutineScope.launch {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = networkErrorMessage
                    )
                }

                ChangeEmailUiEvent.ShowUnknownErrorMessage -> coroutineScope.launch {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = unknownErrorMessage
                    )
                }
            }
        }
    }

    Scaffold(
        topBar = {
            ChangeEmailTopAppBar(
                onCloseClick = { changeEmailViewModel.onEvent(ChangeEmailEvent.CloseClick) },
                onChangeClick = { changeEmailViewModel.onEvent(ChangeEmailEvent.ChangeClick) },
                changing = changeEmailState.value.changing
            )
        },
        scaffoldState = scaffoldState,
        snackbarHost = { scaffoldState.snackbarHostState }
    ) {
        ChangeEmailDisplay(
            email = changeEmailState.value.email,
            password = changeEmailState.value.password,
            onEmailChange = { newEmail -> changeEmailViewModel.onEvent(ChangeEmailEvent.OnEmailChange(newEmail)) },
            onPasswordChange = { newPassword -> changeEmailViewModel.onEvent(ChangeEmailEvent.OnPasswordChange(newPassword)) },
            incorrectEmail = changeEmailState.value.incorrectEmail,
            incorrectPassword = changeEmailState.value.incorrectPassword,
            snackbarHostState = scaffoldState.snackbarHostState
        )
    }
}