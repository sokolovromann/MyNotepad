package ru.sokolovromann.mynotepad.screens

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import ru.sokolovromann.mynotepad.MyNotepadRoute
import ru.sokolovromann.mynotepad.data.repository.SettingsRepository
import ru.sokolovromann.mynotepad.notesGraph
import ru.sokolovromann.mynotepad.settingsGraph
import ru.sokolovromann.mynotepad.ui.theme.MyNotepadTheme
import ru.sokolovromann.mynotepad.welcomeGraph
import javax.inject.Inject

@ExperimentalMaterialApi
@ExperimentalFoundationApi
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject lateinit var settingsRepository: SettingsRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyNotepadTheme(isAppNightTheme()) {
                val navController = rememberNavController()

                NavHost(navController, MyNotepadRoute.Welcome.graph) {
                    welcomeGraph(navController)
                    notesGraph(navController)
                    settingsGraph(navController) { openGitHub() }
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
        var appNightTheme by rememberSaveable {
            mutableStateOf(false)
        }

        LaunchedEffect(true) {
            settingsRepository.getAppNightTheme().collectLatest { appNightTheme = it }
        }
        return appNightTheme
    }
}
