package ru.sokolovromann.mynotepad.screens.changeemail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.sokolovromann.mynotepad.R
import ru.sokolovromann.mynotepad.ui.components.DefaultHelperText
import ru.sokolovromann.mynotepad.ui.components.DefaultSnackbar
import ru.sokolovromann.mynotepad.ui.theme.MyNotepadTheme

@Composable
fun ChangeEmailDisplay(
    email: String,
    onEmailChange: (newEmail: String) -> Unit,
    incorrectEmail: Boolean,
    snackbarHostState: SnackbarHostState
) {
    Box(modifier = Modifier.background(MaterialTheme.colors.surface)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            OutlinedTextField(
                value = email,
                onValueChange = { onEmailChange(it) },
                label = { Text(text = stringResource(id = R.string.change_email_email_label)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                singleLine = true,
                isError = incorrectEmail,
                modifier = Modifier.fillMaxWidth()
            )
            DefaultHelperText(
                helperText = "",
                errorText = stringResource(id = R.string.change_email_incorrect_email_message),
                isError = incorrectEmail,
                modifier = Modifier.padding(start = 16.dp, bottom = 4.dp)
            )
        }
        DefaultSnackbar(
            snackbarHostState = snackbarHostState,
            modifier = Modifier
                .padding(16.dp)
                .align(alignment = Alignment.BottomCenter)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ChangeEmailDisplayPreview() {
    MyNotepadTheme {
        ChangeEmailDisplay(
            email = "new-email@domain.com",
            onEmailChange = {},
            incorrectEmail = true,
            snackbarHostState = rememberScaffoldState().snackbarHostState
        )
    }
}