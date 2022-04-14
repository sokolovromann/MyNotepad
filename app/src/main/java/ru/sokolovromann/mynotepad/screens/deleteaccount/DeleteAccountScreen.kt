package ru.sokolovromann.mynotepad.screens.deleteaccount

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
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
import ru.sokolovromann.mynotepad.MyNotepadRoute
import ru.sokolovromann.mynotepad.R
import ru.sokolovromann.mynotepad.screens.deleteaccount.components.DeleteAccountPasswordField
import ru.sokolovromann.mynotepad.screens.deleteaccount.components.DeleteAccountTopAppBar
import ru.sokolovromann.mynotepad.ui.components.DefaultSnackbar

@Composable
fun DeleteAccountScreen(
    viewModel: DeleteAccountViewModel = hiltViewModel(),
    navController: NavController
) {
    val scaffoldState = rememberScaffoldState()

    val coroutineScope = rememberCoroutineScope()

    val networkErrorMessage = stringResource(id = R.string.delete_account_network_error_message)
    val unknownErrorMessage = stringResource(id = R.string.delete_account_unknown_error_message)

    LaunchedEffect(Unit) {
        viewModel.deleteAccountUiEvent.collectLatest { uiEvent ->
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
                onCloseClick = { viewModel.onEvent(DeleteAccountEvent.CloseClick) },
                onDeleteClick = { viewModel.onEvent(DeleteAccountEvent.DeleteClick) },
                deleting = viewModel.deletingState.value
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
                Text(
                    text = stringResource(id = R.string.delete_account_warning),
                    style = MaterialTheme.typography.body1,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, bottom = 8.dp)
                )
                DeleteAccountPasswordField(
                    fieldState = viewModel.passwordFieldState.value,
                    onValueChange = { viewModel.onEvent(DeleteAccountEvent.OnPasswordChange(it)) }
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