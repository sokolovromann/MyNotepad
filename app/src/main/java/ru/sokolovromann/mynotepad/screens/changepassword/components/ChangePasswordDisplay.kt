package ru.sokolovromann.mynotepad.screens.changepassword.components

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
import ru.sokolovromann.mynotepad.ui.components.DefaultSnackbar
import ru.sokolovromann.mynotepad.ui.theme.MyNotepadTheme

@Composable
fun ChangePasswordDisplay(
    newPassword: String,
    currentPassword: String,
    onNewPasswordChange: (newPassword: String) -> Unit,
    onCurrentPasswordChange: (newPassword: String) -> Unit,
    incorrectNewPassword: Boolean,
    incorrectCurrentPassword: Boolean,
    snackbarHostState: SnackbarHostState
) {
    Box(modifier = Modifier.background(MaterialTheme.colors.surface)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            OutlinedTextField(
                value = newPassword,
                onValueChange = { onNewPasswordChange(it) },
                label = { Text(text = stringResource(id = R.string.change_password_new_password_label)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                visualTransformation = PasswordVisualTransformation(),
                singleLine = true,
                isError = incorrectNewPassword,
                modifier = Modifier.fillMaxWidth()
            )
            DefaultHelperText(
                helperText = stringResource(id = R.string.change_password_new_password_helper),
                errorText = stringResource(id = R.string.change_password_min_length_password_message),
                isError = incorrectNewPassword,
                modifier = Modifier.padding(start = 16.dp, bottom = 4.dp)
            )
            OutlinedTextField(
                value = currentPassword,
                onValueChange = { onCurrentPasswordChange(it) },
                label = { Text(text = stringResource(id = R.string.change_password_current_password_label)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                visualTransformation = PasswordVisualTransformation(),
                singleLine = true,
                isError = incorrectCurrentPassword,
                modifier = Modifier.fillMaxWidth()
            )
            DefaultHelperText(
                helperText = "",
                errorText = stringResource(id = R.string.change_password_incorrect_current_password_message),
                isError = incorrectCurrentPassword,
                modifier = Modifier.padding(start = 16.dp)
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
fun ChangePasswordDisplayPreview() {
    MyNotepadTheme {
        ChangePasswordDisplay(
            newPassword = "abcd12345.",
            currentPassword = "abc12345.",
            onNewPasswordChange = {},
            onCurrentPasswordChange = {},
            incorrectNewPassword = true,
            incorrectCurrentPassword = false,
            snackbarHostState = rememberScaffoldState().snackbarHostState
        )
    }
}