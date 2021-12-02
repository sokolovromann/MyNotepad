package ru.sokolovromann.mynotepad.ui.components

import androidx.compose.material.ButtonColors
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import ru.sokolovromann.mynotepad.ui.theme.MyNotepadTheme

@Composable
fun DefaultTextButton(
    modifier: Modifier = Modifier,
    colors: ButtonColors = ButtonDefaults.textButtonColors(),
    onClick: () -> Unit,
    text: String,
    allCaps: Boolean = false
) {
    TextButton(
        modifier = modifier,
        colors = colors,
        onClick = onClick
    ) {
        Text(text = if (allCaps) text.uppercase() else text)
    }
}

@Composable
fun DefaultTextButton(
    modifier: Modifier = Modifier,
    colors: ButtonColors = ButtonDefaults.textButtonColors(),
    onClick: () -> Unit,
    stringResourceId: Int,
    allCaps: Boolean = false
) {
    TextButton(
        modifier = modifier,
        colors = colors,
        onClick = onClick
    ) {
        Text(text = if (allCaps) {
            stringResource(stringResourceId).uppercase()
        } else {
            stringResource(stringResourceId)
        })
    }
}

@Preview(showBackground = true)
@Composable
private fun DefaultTextButtonPreview() {
    MyNotepadTheme {
        DefaultTextButton(
            onClick = {},
            text = "Text Button",
            allCaps = true
        )
    }
}