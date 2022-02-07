package ru.sokolovromann.mynotepad.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import ru.sokolovromann.mynotepad.ui.theme.MyNotepadTheme

@Composable
fun DefaultTopAppBar(
    modifier: Modifier = Modifier,
    title: @Composable () -> Unit = {},
    navigationIcon: @Composable (() -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {},
    backgroundColor: Color = backgroundColor(),
    contentColor: Color = contentColor(),
    elevation: Dp = AppBarDefaults.TopAppBarElevation
) {
    TopAppBar(
        title = title,
        modifier = modifier,
        navigationIcon = navigationIcon,
        actions = actions,
        backgroundColor = backgroundColor,
        contentColor = contentColor,
        elevation = elevation
    )
}

@Composable
fun AppBarTitleText(
    text: String,
    textColor: Color = contentColor()
) {
    Text(
        text = text,
        color = textColor,
        style = MaterialTheme.typography.h6
    )
}

@Composable
fun AppBarTextButton(
    onClick: () -> Unit,
    text: String,
    textColor: Color = contentColor()
) {
    TextButton(
        colors = ButtonDefaults.buttonColors(
            contentColor = textColor,
            backgroundColor = Color.Unspecified
        ),
        onClick = onClick
    ) {
        Text(text = text.uppercase())
    }
}

@Composable
fun AppBarIconButton(
    onClick: () -> Unit,
    icon: ImageVector,
    iconColor: Color = tint(),
    contentDescription: String = ""
) {
    IconButton(onClick = onClick) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = iconColor
        )
    }
}

@Composable
fun AppBarIconButton(
    onClick: () -> Unit,
    icon: Painter,
    iconColor: Color = tint(),
    contentDescription: String = ""
) {
    IconButton(onClick = onClick) {
        Icon(
            painter = icon,
            contentDescription = contentDescription,
            tint = iconColor
        )
    }
}

@Composable
fun AppBarLoadingIndicator(
    indicatorColor: Color = contentColor(),
    strokeWidth: Dp = ProgressIndicatorDefaults.StrokeWidth
) {
    CircularProgressIndicator(
        color = indicatorColor,
        strokeWidth = strokeWidth
    )
}

@Composable
private fun backgroundColor(): Color {
    return if (MaterialTheme.colors.isLight) {
        MaterialTheme.colors.primary
    } else {
        MaterialTheme.colors.surface
    }
}

@Composable
private fun contentColor(): Color {
    return if (MaterialTheme.colors.isLight) {
        MaterialTheme.colors.onPrimary
    } else {
        MaterialTheme.colors.onSurface
    }
}

@Composable
private fun tint(): Color = contentColor().copy(0.8f)

@Preview(
    name = "Light",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Preview(
    name = "Night",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun AppBarPreview() {
    MyNotepadTheme {
        DefaultTopAppBar(
            title = {
                AppBarTitleText(text = "My Notepad")
            },
            navigationIcon = {
                AppBarIconButton(onClick = {}, icon = Icons.Default.ArrowBack)
            },
            actions = {
                AppBarIconButton(onClick = {}, icon = Icons.Default.Edit)
                AppBarIconButton(onClick = {}, icon = Icons.Default.Delete)
            }
        )
    }
}