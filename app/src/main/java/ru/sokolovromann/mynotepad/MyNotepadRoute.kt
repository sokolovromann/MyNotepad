package ru.sokolovromann.mynotepad

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import ru.sokolovromann.mynotepad.screens.addeditnote.AddEditNoteScreen
import ru.sokolovromann.mynotepad.screens.notes.NotesScreen

sealed class MyNotepadRoute(val graph: String, @StringRes val graphNameResId: Int, @DrawableRes val graphIconResId: Int) {
    object Notes : MyNotepadRoute(graph = "notes", graphNameResId = R.string.drawer_notes, R.drawable.ic_notes_navigation) {
        const val notesScreen = "notesscreen"
        const val addNoteScreen = "addnotescreen"
        fun editNoteScreen(uid: String): String = "editnotescreen/$uid"
    }
    object Settings : MyNotepadRoute(graph = "settings", graphNameResId = R.string.drawer_settings, R.drawable.ic_settings_navigation) {
        const val settingsScreen = "settingsscreen"
    }
}


@ExperimentalFoundationApi
fun NavGraphBuilder.notesGraph(navController: NavController) {
    navigation(startDestination = MyNotepadRoute.Notes.notesScreen, route = MyNotepadRoute.Notes.graph) {
        composable(MyNotepadRoute.Notes.notesScreen) {
            NotesScreen(navController = navController)
        }
        composable(MyNotepadRoute.Notes.addNoteScreen) {
            AddEditNoteScreen(navController = navController)
        }
        composable(MyNotepadRoute.Notes.editNoteScreen("{uid}")) {
            AddEditNoteScreen(navController = navController)
        }
    }
}

fun NavGraphBuilder.settingsGraph(navController: NavController) {
    navigation(startDestination = MyNotepadRoute.Settings.settingsScreen, route = MyNotepadRoute.Settings.graph) {
        composable(MyNotepadRoute.Settings.settingsScreen) {
            // TODO Add SettingsScreen
        }
    }
}