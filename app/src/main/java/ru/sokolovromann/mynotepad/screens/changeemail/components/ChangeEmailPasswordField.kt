package ru.sokolovromann.mynotepad.screens.changeemail.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import ru.sokolovromann.mynotepad.R
import ru.sokolovromann.mynotepad.screens.changeemail.state.ChangeEmailPasswordFieldState
import ru.sokolovromann.mynotepad.ui.components.DefaultHelperText

@Composable
fun ChangeEmailPasswordTextField(
    fieldState: ChangeEmailPasswordFieldState,
    onValueChange: (String) -> Unit
) {
    Column {
        OutlinedTextField(
            value = fieldState.password,
            onValueChange = { onValueChange(it) },
            label = { Text(text = stringResource(id = R.string.change_email_password_label)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = PasswordVisualTransformation(),
            singleLine = true,
            isError = fieldState.showError,
            modifier = Modifier.fillMaxWidth()
        )
        DefaultHelperText(
            helperText = "",
            errorText = stringResource(id = R.string.change_email_incorrect_password_message),
            isError = fieldState.showError,
            modifier = Modifier.padding(start = 16.dp)
        )
    }
}