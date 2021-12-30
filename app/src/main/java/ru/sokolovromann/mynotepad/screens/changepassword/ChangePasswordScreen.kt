package ru.sokolovromann.mynotepad.screens.changepassword

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
import ru.sokolovromann.mynotepad.screens.changepassword.components.ChangePasswordDisplay
import ru.sokolovromann.mynotepad.screens.changepassword.components.ChangePasswordTopAppBar

@Composable
fun ChangePasswordScreen(
    changePasswordViewModel: ChangePasswordViewModel = hiltViewModel(),
    navController: NavController
) {
    val changePasswordState = changePasswordViewModel.changePasswordState
    val scaffoldState = rememberScaffoldState()

    val coroutineScope = rememberCoroutineScope()

    val changedMessage = stringResource(id = R.string.change_password_changed_message)
    val networkErrorMessage = stringResource(id = R.string.change_password_network_error_message)
    val unknownErrorMessage = stringResource(id = R.string.change_password_unknown_error_message)

    LaunchedEffect(true) {
        changePasswordViewModel.changePasswordUiEvent.collectLatest { uiEvent ->
            when (uiEvent) {
                ChangePasswordUiEvent.OpenSettings -> navController.popBackStack()

                ChangePasswordUiEvent.ShowPasswordChangedMessage -> coroutineScope.launch {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = changedMessage
                    )
                }

                ChangePasswordUiEvent.ShowNetworkErrorMessage -> coroutineScope.launch {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = networkErrorMessage
                    )
                }

                ChangePasswordUiEvent.ShowUnknownErrorMessage -> coroutineScope.launch {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = unknownErrorMessage
                    )
                }
            }
        }
    }

    Scaffold(
        topBar = {
            ChangePasswordTopAppBar(
                onCloseClick = { changePasswordViewModel.onEvent(ChangePasswordEvent.CloseClick) },
                onChangeClick = { changePasswordViewModel.onEvent(ChangePasswordEvent.ChangeClick) },
                changing = changePasswordState.value.changing
            )
        },
        scaffoldState = scaffoldState,
        snackbarHost = { scaffoldState.snackbarHostState }
    ) {
        ChangePasswordDisplay(
            newPassword = changePasswordState.value.newPassword,
            currentPassword = changePasswordState.value.currentPassword,
            onNewPasswordChange = { newPassword -> changePasswordViewModel.onEvent(ChangePasswordEvent.OnNewPasswordChange(newPassword)) },
            onCurrentPasswordChange = { newPassword -> changePasswordViewModel.onEvent(ChangePasswordEvent.OnCurrentPasswordChange(newPassword)) },
            incorrectNewPassword = changePasswordState.value.incorrectNewPassword,
            incorrectCurrentPassword = changePasswordState.value.incorrectCurrentPassword,
            snackbarHostState = scaffoldState.snackbarHostState
        )
    }
}