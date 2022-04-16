package ru.sokolovromann.mynotepad.screens.welcome.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import ru.sokolovromann.mynotepad.R

@Composable
fun WelcomeLogoIcon() {
    Surface(
        shape = CircleShape,
        color = MaterialTheme.colors.primary
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_welcome_logo),
            contentDescription = "",
            tint = MaterialTheme.colors.onPrimary,
            modifier = Modifier.padding(16.dp)
        )
    }
}