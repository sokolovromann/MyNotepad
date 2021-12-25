package ru.sokolovromann.mynotepad.screens.signup.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.sokolovromann.mynotepad.R
import ru.sokolovromann.mynotepad.ui.components.DefaultHelperText
import ru.sokolovromann.mynotepad.ui.components.DefaultLoadingIndicator
import ru.sokolovromann.mynotepad.ui.components.DefaultSnackbar
import ru.sokolovromann.mynotepad.ui.theme.MyNotepadTheme

@Composable
fun SignUpDisplay(
    email: String,
    password: String,
    onEmailChange: (newEmail: String) -> Unit,
    onPasswordChange: (newPassword: String) -> Unit,
    onCreateAccountClick: () -> Unit,
    incorrectEmail: Boolean,
    incorrectMinLengthPassword: Boolean,
    creatingAccount: Boolean,
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
                label = { Text(text = stringResource(id = R.string.sign_up_email_label)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                singleLine = true,
                isError = incorrectEmail,
                modifier = Modifier
                    .fillMaxWidth()
            )
            DefaultHelperText(
                helperText = "",
                errorText = stringResource(id = R.string.sign_up_incorrect_email_message),
                isError = incorrectEmail,
                modifier = Modifier.padding(start = 16.dp, bottom = 4.dp)
            )
            OutlinedTextField(
                value = password,
                onValueChange = { onPasswordChange(it) },
                label = { Text(text = stringResource(id = R.string.sign_up_password_label)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                visualTransformation = PasswordVisualTransformation(),
                singleLine = true,
                isError = incorrectMinLengthPassword,
                modifier = Modifier
                    .fillMaxWidth()
            )
            DefaultHelperText(
                helperText = stringResource(id = R.string.sign_up_password_helper),
                errorText = stringResource(id = R.string.sign_up_incorrect_min_length_password_message),
                isError = incorrectMinLengthPassword,
                modifier = Modifier.padding(start = 16.dp, bottom = 16.dp)
            )
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (creatingAccount) {
                    DefaultLoadingIndicator()
                } else {
                    OutlinedButton(
                        onClick = onCreateAccountClick,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = stringResource(id = R.string.sign_up_create_account))
                    }
                }
            }

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
private fun SignUpDisplayPreview() {
    MyNotepadTheme {
        SignUpDisplay(
            email = "email@domain.com",
            password = "abc12345.",
            onEmailChange = {},
            onPasswordChange = {},
            onCreateAccountClick = {},
            incorrectEmail = true,
            incorrectMinLengthPassword = false,
            creatingAccount = false,
            snackbarHostState = rememberScaffoldState().snackbarHostState
        )
    }
}