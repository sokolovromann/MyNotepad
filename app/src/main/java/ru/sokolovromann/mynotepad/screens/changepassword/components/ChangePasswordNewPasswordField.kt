package ru.sokolovromann.mynotepad.screens.changepassword.components

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
import ru.sokolovromann.mynotepad.screens.changepassword.state.NewPasswordFieldState
import ru.sokolovromann.mynotepad.ui.components.DefaultHelperText

@Composable
fun ChangePasswordNewPasswordField(
    fieldState: NewPasswordFieldState,
    onValueChange: (String) -> Unit
) {
    Column {
        OutlinedTextField(
            value = fieldState.newPassword,
            onValueChange = { onValueChange(it) },
            label = { Text(text = stringResource(id = R.string.change_password_new_password_label)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = PasswordVisualTransformation(),
            singleLine = true,
            isError = fieldState.showError,
            modifier = Modifier.fillMaxWidth()
        )
        DefaultHelperText(
            helperText = stringResource(id = R.string.change_password_new_password_helper),
            errorText = stringResource(id = R.string.change_password_min_length_password_message),
            isError = fieldState.showError,
            modifier = Modifier.padding(start = 16.dp, bottom = 4.dp)
        )
    }
}