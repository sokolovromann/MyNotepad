package ru.sokolovromann.mynotepad.screens.changeemail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
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
import ru.sokolovromann.mynotepad.screens.changeemail.components.ChangeEmailEmailTextField
import ru.sokolovromann.mynotepad.screens.changeemail.components.ChangeEmailPasswordTextField
import ru.sokolovromann.mynotepad.screens.changeemail.components.ChangeEmailTopAppBar
import ru.sokolovromann.mynotepad.ui.components.DefaultSnackbar

@Composable
fun ChangeEmailScreen(
    viewModel: ChangeEmailViewModel = hiltViewModel(),
    navController: NavController
) {
    val scaffoldState = rememberScaffoldState()

    val coroutineScope = rememberCoroutineScope()

    val changedMessage = stringResource(id = R.string.change_email_changed_message)
    val networkErrorMessage = stringResource(id = R.string.change_email_network_error_message)
    val unknownErrorMessage = stringResource(id = R.string.change_email_unknown_error_message)

    LaunchedEffect(Unit) {
        viewModel.changeEmailUiEvent.collectLatest { uiEvent ->
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
                onCloseClick = { viewModel.onEvent(ChangeEmailEvent.CloseClick) },
                onChangeClick = { viewModel.onEvent(ChangeEmailEvent.ChangeClick) },
                changing = viewModel.changingState.value
            )
        },
        scaffoldState = scaffoldState,
        snackbarHost = { scaffoldState.snackbarHostState }
    ) {
        Box(modifier = Modifier.background(MaterialTheme.colors.surface)) {
            Column(modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
            ) {
                ChangeEmailEmailTextField(
                    fieldState = viewModel.emailFieldState.value,
                    onValueChange = { viewModel.onEvent(ChangeEmailEvent.OnEmailChange(it)) }
                )
                ChangeEmailPasswordTextField(
                    fieldState = viewModel.passwordFieldState.value,
                    onValueChange = { viewModel.onEvent(ChangeEmailEvent.OnPasswordChange(it)) }
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