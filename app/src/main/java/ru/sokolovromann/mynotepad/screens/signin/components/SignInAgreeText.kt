package ru.sokolovromann.mynotepad.screens.signin.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import ru.sokolovromann.mynotepad.R

@Composable
fun SignInAgreeText() {
    val agreeText = stringResource(id = R.string.sign_in_agree).split("\n")
    val text = buildAnnotatedString {
        withStyle(style = SpanStyle(color = MaterialTheme.colors.onSurface)) {
            append(agreeText[0])
        }
        pushStringAnnotation(
            tag = "TERMS",
            annotation = stringResource(id = R.string.app_terms_link)
        )
        withStyle(style = SpanStyle(color = MaterialTheme.colors.primary)) {
            append(agreeText[1])
        }
        withStyle(style = SpanStyle(color = MaterialTheme.colors.onSurface)) {
            append(agreeText[2])
        }
        pop()

        pushStringAnnotation(
            tag = "PRIVACY_POLICY",
            annotation = stringResource(id = R.string.app_privacy_policy_link)
        )
        withStyle(style = SpanStyle(color = MaterialTheme.colors.primary)) {
            append(agreeText[3])
        }
        pop()
    }

    val uriHandler = LocalUriHandler.current
    ClickableText(
        text = text,
        modifier = Modifier.padding(start = 8.dp, end = 8.dp, bottom = 8.dp),
        onClick = { offset ->
            text.getStringAnnotations(tag = "TERMS", start = offset, end = offset).firstOrNull()?.let {
                uriHandler.openUri(it.item)
            }

            text.getStringAnnotations(tag = "PRIVACY_POLICY", start = offset, end = offset).firstOrNull()?.let {
                uriHandler.openUri(it.item)
            }
        }
    )
}