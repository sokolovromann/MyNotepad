package ru.sokolovromann.mynotepad.screens.welcome.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.sokolovromann.mynotepad.R
import ru.sokolovromann.mynotepad.ui.components.DefaultTextButton
import ru.sokolovromann.mynotepad.ui.theme.MyNotepadTheme

@Composable
fun WelcomeDisplay(
    onSignUpClick: () -> Unit,
    onSignInClick: () -> Unit,
    onContinueWithoutSyncClick: () -> Unit
) {
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
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(
                shape = CircleShape,
                color = MaterialTheme.colors.primary
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_welcome_logo),
                    tint = MaterialTheme.colors.onPrimary,
                    contentDescription = "",
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
        Column(modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(id = R.string.welcome_title),
                color = MaterialTheme.colors.onSurface,
                style = MaterialTheme.typography.h5,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            DefaultTextButton(
                onClick = onSignUpClick,
                text = stringResource(id = R.string.welcome_sign_up)
            )
            DefaultTextButton(
                onClick = onSignInClick,
                text = stringResource(id = R.string.welcome_sign_in)
            )
            DefaultTextButton(
                onClick = onContinueWithoutSyncClick,
                text = stringResource(id = R.string.welcome_continue_without_sync)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun WelcomeDisplayPreview() {
    MyNotepadTheme {
        WelcomeDisplay(
            onSignUpClick = {},
            onSignInClick = {},
            onContinueWithoutSyncClick = {}
        )
    }
}