package ru.sokolovromann.mynotepad.screens

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import ru.sokolovromann.mynotepad.MyNotepadRoute
import ru.sokolovromann.mynotepad.notesGraph
import ru.sokolovromann.mynotepad.screens.settings.SettingsState
import ru.sokolovromann.mynotepad.screens.settings.SettingsViewModel
import ru.sokolovromann.mynotepad.settingsGraph
import ru.sokolovromann.mynotepad.ui.theme.MyNotepadTheme
import ru.sokolovromann.mynotepad.welcomeGraph

@ExperimentalFoundationApi
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyNotepadTheme(darkTheme = isAppNightTheme()) {
                val navController = rememberNavController()

                NavHost(navController = navController, startDestination = MyNotepadRoute.Welcome.graph) {
                    welcomeGraph(
                        navController = navController
                    )
                    notesGraph(
                        navController = navController
                    )
                    settingsGraph(
                        navController = navController,
                        onOpenGitHub = { openGitHub() }
                    )
                }
            }
        }
    }

    private fun openGitHub() {
        val intent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("https://github.com/sokolovromann/MyNotepad")
        )
        startActivity(intent)
    }

    @Composable
    private fun isAppNightTheme(): Boolean {
        val settingsViewModel: SettingsViewModel = hiltViewModel()
        val settingsState = settingsViewModel.settingsState.value

        var appNightTheme = false
        if (settingsState is SettingsState.SettingsDisplay) {
            appNightTheme = settingsState.settings.appNightTheme
        }

        return appNightTheme
    }
}
