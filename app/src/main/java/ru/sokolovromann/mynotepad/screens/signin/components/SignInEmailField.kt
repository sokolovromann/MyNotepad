package ru.sokolovromann.mynotepad.screens.signin.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import ru.sokolovromann.mynotepad.R
import ru.sokolovromann.mynotepad.screens.signin.state.SignInEmailFieldState
import ru.sokolovromann.mynotepad.ui.components.DefaultHelperText

@Composable
fun SignInEmailField(
    fieldState: SignInEmailFieldState,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = fieldState.email,
        onValueChange = { onValueChange(it) },
        label = { Text(text = stringResource(id = R.string.sign_in_email_label)) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        singleLine = true,
        isError = fieldState.showError,
        modifier = Modifier.fillMaxWidth()
    )
    DefaultHelperText(
        helperText = "",
        errorText = stringResource(id = R.string.sign_in_incorrect_email_message),
        isError = fieldState.showError,
        modifier = Modifier.padding(start = 8.dp, bottom = 4.dp)
    )
}