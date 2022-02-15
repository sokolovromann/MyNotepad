package ru.sokolovromann.mynotepad

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import ru.sokolovromann.mynotepad.screens.addeditnote.AddEditNoteScreen
import ru.sokolovromann.mynotepad.screens.changeemail.ChangeEmailScreen
import ru.sokolovromann.mynotepad.screens.changepassword.ChangePasswordScreen
import ru.sokolovromann.mynotepad.screens.deleteaccount.DeleteAccountScreen
import ru.sokolovromann.mynotepad.screens.notes.NotesScreen
import ru.sokolovromann.mynotepad.screens.settings.SettingsScreen
import ru.sokolovromann.mynotepad.screens.signin.SignInScreen
import ru.sokolovromann.mynotepad.screens.signup.SignUpScreen
import ru.sokolovromann.mynotepad.screens.welcome.WelcomeScreen

sealed class MyNotepadRoute(val graph: String, @StringRes val graphNameResId: Int, @DrawableRes val graphIconResId: Int) {
    object Notes : MyNotepadRoute(graph = "notes", graphNameResId = R.string.drawer_notes, R.drawable.ic_notes_navigation) {
        const val notesScreen = "notesscreen"
        const val addNoteScreen = "addnotescreen"
        fun editNoteScreen(uid: String): String = "editnotescreen/$uid"
    }
    object Settings : MyNotepadRoute(graph = "settings", graphNameResId = R.string.drawer_settings, R.drawable.ic_settings_navigation) {
        const val settingsScreen = "settingsscreen"
        const val changeEmailScreen = "changeemailscreen"
        const val changePasswordScreen = "changepasswordscreen"
        const val deleteAccountScreen = "deleteaccountscreen"
    }
    object Welcome : MyNotepadRoute(graph = "welcome", graphNameResId = 0, graphIconResId = 0) {
        const val welcomeScreen = "welcomescreen"
        const val signUpScreen = "signupscreen"
        const val signInScreen = "signinscreen"
    }
}

@ExperimentalMaterialApi
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

@ExperimentalFoundationApi
fun NavGraphBuilder.settingsGraph(
    navController: NavController,
    onOpenGitHub: () -> Unit,
    onOpenEmailApp: () -> Unit,
    onOpenTerms: () -> Unit,
    onOpenPrivacyPolicy: () -> Unit
) {
    navigation(startDestination = MyNotepadRoute.Settings.settingsScreen, route = MyNotepadRoute.Settings.graph) {
        composable(MyNotepadRoute.Settings.settingsScreen) {
            SettingsScreen(
                navController = navController,
                onOpenGitHub = onOpenGitHub,
                onOpenEmailApp = onOpenEmailApp,
                onOpenTerms = onOpenTerms,
                onOpenPrivacyPolicy = onOpenPrivacyPolicy
            )
        }
        composable(MyNotepadRoute.Settings.changeEmailScreen) {
            ChangeEmailScreen(navController = navController)
        }
        composable(MyNotepadRoute.Settings.changePasswordScreen) {
            ChangePasswordScreen(navController = navController)
        }
        composable(MyNotepadRoute.Settings.deleteAccountScreen) {
            DeleteAccountScreen(navController = navController)
        }
    }
}

fun NavGraphBuilder.welcomeGraph(navController: NavController) {
    navigation(startDestination = MyNotepadRoute.Welcome.welcomeScreen, route = MyNotepadRoute.Welcome.graph) {
        composable(MyNotepadRoute.Welcome.welcomeScreen) {
            WelcomeScreen(navController = navController)
        }
        composable(MyNotepadRoute.Welcome.signUpScreen) {
            SignUpScreen(navController = navController)
        }
        composable(MyNotepadRoute.Welcome.signInScreen) {
            SignInScreen(navController = navController)
        }
    }
}