package ru.sokolovromann.mynotepad.screens.deleteaccount

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
import ru.sokolovromann.mynotepad.MyNotepadRoute
import ru.sokolovromann.mynotepad.R
import ru.sokolovromann.mynotepad.screens.deleteaccount.components.DeleteAccountDisplay
import ru.sokolovromann.mynotepad.screens.deleteaccount.components.DeleteAccountTopAppBar

@Composable
fun DeleteAccountScreen(
    deleteAccountViewModel: DeleteAccountViewModel = hiltViewModel(),
    navController: NavController
) {
    val deleteAccountState = deleteAccountViewModel.deleteAccountState
    val scaffoldState = rememberScaffoldState()

    val coroutineScope = rememberCoroutineScope()

    val networkErrorMessage = stringResource(id = R.string.delete_account_network_error_message)
    val unknownErrorMessage = stringResource(id = R.string.delete_account_unknown_error_message)

    LaunchedEffect(true) {
        deleteAccountViewModel.deleteAccountUiEvent.collectLatest { uiEvent ->
            when (uiEvent) {
                DeleteAccountUiEvent.OpenWelcome -> navController.navigate(MyNotepadRoute.Welcome.welcomeScreen) {
                    navController.backQueue.clear()
                }

                DeleteAccountUiEvent.OpenSettings -> navController.popBackStack()

                DeleteAccountUiEvent.ShowNetworkErrorMessage -> coroutineScope.launch {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = networkErrorMessage
                    )
                }

                DeleteAccountUiEvent.ShowUnknownErrorMessage -> coroutineScope.launch {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = unknownErrorMessage
                    )
                }
            }
        }
    }

    Scaffold(
        topBar = {
            DeleteAccountTopAppBar(
                onCloseClick = { deleteAccountViewModel.onEvent(DeleteAccountEvent.CloseClick) },
                onDeleteClick = { deleteAccountViewModel.onEvent(DeleteAccountEvent.DeleteClick) },
                deleting = deleteAccountState.value.deleting
            )
        },
        scaffoldState = scaffoldState,
        snackbarHost = { scaffoldState.snackbarHostState }
    ) {
        DeleteAccountDisplay(
            password = deleteAccountState.value.password,
            onPasswordChange = { newPassword -> deleteAccountViewModel.onEvent(DeleteAccountEvent.OnPasswordChange(newPassword)) },
            incorrectPassword = deleteAccountState.value.incorrectPassword,
            snackbarHostState = scaffoldState.snackbarHostState
        )
    }
}