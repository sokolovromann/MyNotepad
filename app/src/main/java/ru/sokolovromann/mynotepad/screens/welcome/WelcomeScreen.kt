package ru.sokolovromann.mynotepad.screens.welcome

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import kotlinx.coroutines.flow.collectLatest
import ru.sokolovromann.mynotepad.MyNotepadRoute
import ru.sokolovromann.mynotepad.R
import ru.sokolovromann.mynotepad.screens.welcome.components.WelcomeLogoIcon
import ru.sokolovromann.mynotepad.ui.components.CircularLoading
import ru.sokolovromann.mynotepad.ui.components.DefaultTextButton

@Composable
fun WelcomeScreen(
    viewModel: WelcomeViewModel = hiltViewModel(),
    navController: NavController
) {
    val scaffoldState = rememberScaffoldState()

    LaunchedEffect(Unit) {
        viewModel.welcomeUiEvent.collectLatest { uiEvent ->
            when (uiEvent) {
                WelcomeUiEvent.OpenSignUp -> navController.navigate(MyNotepadRoute.Welcome.signUpScreen)

                WelcomeUiEvent.OpenSignIn -> navController.navigate(MyNotepadRoute.Welcome.signInScreen)

                WelcomeUiEvent.OpenNotes -> navController.navigate(MyNotepadRoute.Notes.notesScreen) {
                    navController.backQueue.clear()
                }
            }
        }
    }

    Scaffold(scaffoldState = scaffoldState) {
        if (viewModel.loadingState.value) {
            CircularLoading(modifier = Modifier.fillMaxSize())
            return@Scaffold
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.surface)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                content = { WelcomeLogoIcon() }
            )
            Column(modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(id = R.string.welcome_title),
                    color = MaterialTheme.colors.onSurface,
                    style = MaterialTheme.typography.h5,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                DefaultTextButton(
                    onClick = { viewModel.onEvent(WelcomeEvent.SignUpClick) },
                    text = stringResource(id = R.string.welcome_sign_up)
                )
                DefaultTextButton(
                    onClick = { viewModel.onEvent(WelcomeEvent.SignInClick) },
                    text = stringResource(id = R.string.welcome_sign_in)
                )
                DefaultTextButton(
                    onClick = { viewModel.onEvent(WelcomeEvent.ContinueWithoutSyncClick) },
                    text = stringResource(id = R.string.welcome_continue_without_sync)
                )
            }
        }
    }
}