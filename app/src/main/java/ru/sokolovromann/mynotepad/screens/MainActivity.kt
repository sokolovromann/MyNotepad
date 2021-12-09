package ru.sokolovromann.mynotepad.screens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import ru.sokolovromann.mynotepad.MyNotepadRoute
import ru.sokolovromann.mynotepad.notesGraph
import ru.sokolovromann.mynotepad.settingsGraph
import ru.sokolovromann.mynotepad.ui.theme.MyNotepadTheme

@ExperimentalFoundationApi
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyNotepadTheme {
                val navController = rememberNavController()

                NavHost(navController = navController, startDestination = MyNotepadRoute.Notes.graph) {
                    notesGraph(navController)
                    settingsGraph(navController)
                }
            }
        }
    }
}
