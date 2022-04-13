package ru.sokolovromann.mynotepad.screens.changepassword

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.sokolovromann.mynotepad.R
import ru.sokolovromann.mynotepad.screens.changepassword.components.ChangePasswordCurrentPasswordField
import ru.sokolovromann.mynotepad.screens.changepassword.components.ChangePasswordNewPasswordField
import ru.sokolovromann.mynotepad.screens.changepassword.components.ChangePasswordTopAppBar
import ru.sokolovromann.mynotepad.ui.components.DefaultSnackbar

@Composable
fun ChangePasswordScreen(
    viewModel: ChangePasswordViewModel = hiltViewModel(),
    navController: NavController
) {
    val scaffoldState = rememberScaffoldState()

    val coroutineScope = rememberCoroutineScope()

    val changedMessage = stringResource(id = R.string.change_password_changed_message)
    val networkErrorMessage = stringResource(id = R.string.change_password_network_error_message)
    val unknownErrorMessage = stringResource(id = R.string.change_password_unknown_error_message)

    LaunchedEffect(Unit) {
        viewModel.changePasswordUiEvent.collectLatest { uiEvent ->
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
                onCloseClick = { viewModel.onEvent(ChangePasswordEvent.CloseClick) },
                onChangeClick = { viewModel.onEvent(ChangePasswordEvent.ChangeClick) },
                changing = viewModel.changingState.value
            )
        },
        scaffoldState = scaffoldState,
        snackbarHost = { scaffoldState.snackbarHostState }
    ) {
        Box(modifier = Modifier.background(MaterialTheme.colors.surface)) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
            ) {
                ChangePasswordNewPasswordField(
                    fieldState = viewModel.newPasswordFieldState.value,
                    onValueChange = { viewModel.onEvent(ChangePasswordEvent.OnNewPasswordChange(it)) }
                )
                ChangePasswordCurrentPasswordField(
                    fieldState = viewModel.currentPasswordFieldState.value,
                    onValueChange = { viewModel.onEvent(ChangePasswordEvent.OnCurrentPasswordChange(it)) }
                )
            }
            DefaultSnackbar(
                snackbarHostState = scaffoldState.snackbarHostState,
                modifier = Modifier
                    .padding(16.dp)
                    .align(alignment = Alignment.BottomCenter)
            )
        }
    }
}