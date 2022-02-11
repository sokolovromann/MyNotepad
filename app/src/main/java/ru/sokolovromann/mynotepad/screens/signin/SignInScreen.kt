package ru.sokolovromann.mynotepad.screens.signin

import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.sokolovromann.mynotepad.MyNotepadRoute
import ru.sokolovromann.mynotepad.R
import ru.sokolovromann.mynotepad.screens.signin.components.SignInDisplay
import ru.sokolovromann.mynotepad.screens.signin.components.SignInTopAppBar

@Composable
fun SignInScreen(
    signInViewModel: SignInViewModel = hiltViewModel(),
    navController: NavController
) {
    val signInState = signInViewModel.signInState
    val scaffoldState = rememberScaffoldState()

    val coroutineScope = rememberCoroutineScope()

    val networkErrorMessage = stringResource(id = R.string.sign_in_network_error_message)
    val signInErrorMessage = stringResource(id = R.string.sign_in_sign_in_error_message)
    val unknownErrorMessage = stringResource(id = R.string.sign_in_unknown_error_message)
    val resetPasswordMessage = stringResource(id = R.string.sign_in_reset_password_message)

    LaunchedEffect(true) {
        signInViewModel.signInUiEvent.collectLatest { uiEvent ->
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
                onCloseClick = { signInViewModel.onEvent(SignInEvent.CloseClick) }
            )
        },
        scaffoldState = scaffoldState,
        snackbarHost = { scaffoldState.snackbarHostState }
    ) {
        SignInDisplay(
            email = signInState.value.email,
            password = signInState.value.password,
            onEmailChange = { newEmail -> signInViewModel.onEvent(SignInEvent.OnEmailChange(newEmail)) },
            onPasswordChange = { newPassword -> signInViewModel.onEvent(SignInEvent.OnPasswordChange(newPassword)) },
            onSignInClick = { signInViewModel.onEvent(SignInEvent.SignInClick) },
            onResetPassword = { signInViewModel.onEvent(SignInEvent.ResetPasswordClick) },
            incorrectEmail = signInState.value.incorrectEmail,
            incorrectPassword = signInState.value.incorrectPassword,
            signingIn = signInState.value.signingIn,
            snackbarHostState = scaffoldState.snackbarHostState
        )
    }
}