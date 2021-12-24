package ru.sokolovromann.mynotepad.screens.welcome

import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import kotlinx.coroutines.flow.collectLatest
import ru.sokolovromann.mynotepad.MyNotepadRoute
import ru.sokolovromann.mynotepad.screens.welcome.components.WelcomeDisplay

@Composable
fun WelcomeScreen(
    welcomeViewModel: WelcomeViewModel = hiltViewModel(),
    navController: NavController
) {
    val welcomeState = welcomeViewModel.welcomeState
    val scaffoldState = rememberScaffoldState()

    LaunchedEffect(true) {
        welcomeViewModel.welcomeUiEvent.collectLatest { uiEvent ->
            when (uiEvent) {
                WelcomeUiEvent.OpenSignUp -> {
                    // TODO Add OpenSignUp
                }
                WelcomeUiEvent.OpenSignIn -> {
                    // TODO Add OpenSignIn
                }
                WelcomeUiEvent.OpenNotes -> navController.navigate(MyNotepadRoute.Notes.notesScreen) {
                    popUpTo(MyNotepadRoute.Welcome.welcomeScreen) {
                        inclusive = true
                    }
                }
            }
        }
    }

    Scaffold(scaffoldState = scaffoldState) {
        when (welcomeState.value) {
            WelcomeState.Welcome -> WelcomeDisplay(
                onSignUpClick = { welcomeViewModel.onEvent(WelcomeEvent.SignUpClick) },
                onSignInClick = { welcomeViewModel.onEvent(WelcomeEvent.SignInClick) },
                onContinueWithoutSyncClick = { welcomeViewModel.onEvent(WelcomeEvent.ContinueWithoutSyncClick) }
            )
            else -> {}
        }
    }
}