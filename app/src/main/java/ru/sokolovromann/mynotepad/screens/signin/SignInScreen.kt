package ru.sokolovromann.mynotepad.screens.signin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import ru.sokolovromann.mynotepad.MyNotepadRoute
import ru.sokolovromann.mynotepad.R
import ru.sokolovromann.mynotepad.screens.signin.components.*
import ru.sokolovromann.mynotepad.ui.components.CircularLoading
import ru.sokolovromann.mynotepad.ui.components.DefaultSnackbar
import ru.sokolovromann.mynotepad.ui.components.DefaultTextButton

@Composable
fun SignInScreen(
    viewModel: SignInViewModel = hiltViewModel(),
    navController: NavController
) {
    val scrollState = rememberScrollState()
    val scaffoldState = rememberScaffoldState()

    val coroutineScope = rememberCoroutineScope()

    val networkErrorMessage = stringResource(id = R.string.sign_in_network_error_message)
    val signInErrorMessage = stringResource(id = R.string.sign_in_sign_in_error_message)
    val unknownErrorMessage = stringResource(id = R.string.sign_in_unknown_error_message)
    val resetPasswordMessage = stringResource(id = R.string.sign_in_reset_password_message)

    LaunchedEffect(Unit) {
        viewModel.signInUiEvent.collectLatest { uiEvent ->
            when (uiEvent) {
                SignInUiEvent.OpenNotes -> navController.navigate(MyNotepadRoute.Notes.notesScreen) {
                    navController.backQueue.clear()
                }

                SignInUiEvent.OpenWelcome -> navController.popBackStack()

                SignInUiEvent.ShowNetworkErrorMessage -> coroutineScope.launch {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = networkErrorMessage,
                        duration = SnackbarDuration.Long
                    )
                }

                SignInUiEvent.ShowSignInErrorMessage -> coroutineScope.launch {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = signInErrorMessage,
                        duration = SnackbarDuration.Long
                    )
                }

                SignInUiEvent.ShowUnknownErrorMessage -> coroutineScope.launch {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = unknownErrorMessage,
                        duration = SnackbarDuration.Long
                    )
                }

                SignInUiEvent.ShowResetPasswordMessage -> coroutineScope.launch {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = resetPasswordMessage,
                        duration = SnackbarDuration.Long
                    )
                }
            }
        }
    }

    Scaffold(
        topBar = {
            SignInTopAppBar(
                onCloseClick = { viewModel.onEvent(SignInEvent.CloseClick) }
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
                    .verticalScroll(scrollState)
            ) {
                SignInEmailField(
                    fieldState = viewModel.emailFieldState.value,
                    onValueChange = { viewModel.onEvent(SignInEvent.OnEmailChange(it)) }
                )
                SignInPasswordField(
                    fieldState = viewModel.passwordFieldState.value,
                    onValueChange = { viewModel.onEvent(SignInEvent.OnPasswordChange(it)) }
                )
                DefaultTextButton(
                    onClick = { viewModel.onEvent(SignInEvent.ResetPasswordClick) },
                    text = stringResource(id = R.string.sign_in_reset_password),
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
                SignInAgreeText()
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (viewModel.signingIn.value) {
                        CircularLoading()
                    } else {
                        OutlinedButton(
                            onClick = { viewModel.onEvent(SignInEvent.SignInClick) },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(text = stringResource(id = R.string.sign_in_sign_in))
                        }
                    }
                }

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