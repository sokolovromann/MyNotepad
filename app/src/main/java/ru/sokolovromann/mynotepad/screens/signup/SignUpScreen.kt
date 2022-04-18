package ru.sokolovromann.mynotepad.screens.signup

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
import ru.sokolovromann.mynotepad.screens.signup.components.*
import ru.sokolovromann.mynotepad.ui.components.CircularLoading
import ru.sokolovromann.mynotepad.ui.components.DefaultSnackbar

@Composable
fun SignUpScreen(
    viewModel: SignUpViewModel = hiltViewModel(),
    navController: NavController
) {
    val scaffoldState = rememberScaffoldState()
    val scrollState = rememberScrollState()

    val coroutineScope = rememberCoroutineScope()

    val networkErrorMessage = stringResource(id = R.string.sign_up_network_error_message)
    val unknownErrorMessage = stringResource(id = R.string.sign_up_unknown_error_message)

    LaunchedEffect(Unit) {
        viewModel.signUpUiEvent.collectLatest { uiEvent ->
            when (uiEvent) {
                SignUpUiEvent.OpenNotes -> navController.navigate(MyNotepadRoute.Notes.notesScreen) {
                    navController.backQueue.clear()
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
                onCloseClick = { viewModel.onEvent(SignUpEvent.CloseClick) }
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
                SignUpEmailField(
                    fieldState = viewModel.emailFieldState.value,
                    onValueChange = { viewModel.onEvent(SignUpEvent.OnEmailChange(it)) }
                )
                SignUpPasswordField(
                    fieldState = viewModel.passwordFieldState.value,
                    onValueChange = { viewModel.onEvent(SignUpEvent.OnPasswordChange(it)) }
                )
                SignUpAgreeText()
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (viewModel.creatingState.value) {
                        CircularLoading()
                    } else {
                        OutlinedButton(
                            onClick = { viewModel.onEvent(SignUpEvent.CreateAccountClick) },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(text = stringResource(id = R.string.sign_up_create_account))
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