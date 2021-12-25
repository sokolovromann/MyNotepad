package ru.sokolovromann.mynotepad.screens.signup

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
import ru.sokolovromann.mynotepad.screens.signup.components.SignUpDisplay
import ru.sokolovromann.mynotepad.screens.signup.components.SignUpTopAppBar

@Composable
fun SignUpScreen(
    signUpViewModel: SignUpViewModel = hiltViewModel(),
    navController: NavController
) {
    val signUpState = signUpViewModel.signUpState
    val scaffoldState = rememberScaffoldState()

    val coroutineScope = rememberCoroutineScope()

    val networkErrorMessage = stringResource(id = R.string.sign_up_network_error_message)
    val unknownErrorMessage = stringResource(id = R.string.sign_up_unknown_error_message)

    LaunchedEffect(true) {
        signUpViewModel.signUpUiEvent.collectLatest { uiEvent ->
            when (uiEvent) {
                SignUpUiEvent.OpenNotes -> navController.navigate(MyNotepadRoute.Notes.notesScreen) {
                    popUpTo(MyNotepadRoute.Welcome.welcomeScreen) {
                        inclusive = true
                    }
                }
                SignUpUiEvent.OpenWelcome -> navController.popBackStack()
                SignUpUiEvent.ShowNetworkErrorMessage -> coroutineScope.launch {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = networkErrorMessage,
                        duration = SnackbarDuration.Long
                    )
                }
                SignUpUiEvent.ShowUnknownErrorMessage -> coroutineScope.launch {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = unknownErrorMessage,
                        duration = SnackbarDuration.Long
                    )
                }
            }
        }
    }

    Scaffold(
        topBar = {
            SignUpTopAppBar(
                onCloseClick = { signUpViewModel.onEvent(SignUpEvent.CloseClick) }
            )
        },
        scaffoldState = scaffoldState,
        snackbarHost = { scaffoldState.snackbarHostState }
    ) {
        SignUpDisplay(
            email = signUpState.value.email,
            password = signUpState.value.password,
            onEmailChange = { newEmail -> signUpViewModel.onEvent(SignUpEvent.OnEmailChange(newEmail)) },
            onPasswordChange = { newPassword -> signUpViewModel.onEvent(SignUpEvent.OnPasswordChange(newPassword)) },
            onCreateAccountClick = { signUpViewModel.onEvent(SignUpEvent.CreateAccountClick) },
            incorrectEmail = signUpState.value.incorrectEmail,
            incorrectMinLengthPassword = signUpState.value.incorrectMinLengthPassword,
            creatingAccount = signUpState.value.creatingAccount,
            snackbarHostState = scaffoldState.snackbarHostState
        )
    }
}