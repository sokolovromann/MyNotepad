package ru.sokolovromann.mynotepad.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import ru.sokolovromann.mynotepad.MyNotepadRoute
import ru.sokolovromann.mynotepad.R
import ru.sokolovromann.mynotepad.ui.theme.MyNotepadTheme

@Composable
fun NavigationDrawer(
    navController: NavController,
    closeNavigation: () -> Unit,
    drawerHeader: @Composable () -> Unit = {}
) {
    val items = listOf(
        MyNotepadRoute.Notes,
        MyNotepadRoute.Settings
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()

    Column(modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colors.background)
    ) {
        drawerHeader()
        TransparentDivider(thickness = 8.dp)
        items.forEach { item ->
            val selected = navBackStackEntry?.destination?.hierarchy?.any { it.route == item.graph } ?: false
            NavigationDrawerItem(
                text = stringResource(id = item.graphNameResId),
                painterIcon = painterResource(id = item.graphIconResId),
                itemSelected = selected,
                onItemClick = {
                    navController.navigate(item.graph) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                    closeNavigation()
                }
            )
        }
    }
}

@Composable
fun NavigationDrawerHeader(
    title: String,
    description: String = ""
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .padding(start = 16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.h6
        )
        if (description.isNotEmpty()) {
            Text(
                text = description,
                style = MaterialTheme.typography.body2
            )
        }
    }
}

@Composable
private fun NavigationDrawerItem(
    text: String,
    painterIcon: Painter,
    itemSelected: Boolean,
    onItemClick: () -> Unit
) {
    val itemColor = if (itemSelected) {
        MaterialTheme.colors.surface
    } else {
        Color.Transparent
    }

    val textColor = if (itemSelected) {
        MaterialTheme.colors.secondary
    } else {
        MaterialTheme.colors.onSurface
    }

    val iconColor = if (itemSelected) {
        MaterialTheme.colors.secondary
    } else {
        MaterialTheme.colors.onSurface
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .padding(start = 8.dp)
            .background(color = itemColor, shape = MaterialTheme.shapes.medium)
            .clickable { onItemClick() },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Icon(
            painter = painterIcon,
            contentDescription = "",
            tint = iconColor,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Text(
            text = text,
            color = textColor,
            style = MaterialTheme.typography.body1
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun NavigationDrawerPreview() {
    MyNotepadTheme {
        NavigationDrawer(
            navController = rememberNavController(),
            closeNavigation = {},
            drawerHeader = {
                NavigationDrawerHeader(
                    title = stringResource(id = R.string.app_name),
                    description = "useremail@domain.com"
                )
            }
        )
    }
}