package ru.sokolovromann.mynotepad.screens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.ExperimentalMaterialApi
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import ru.sokolovromann.mynotepad.screens.notes.NotesScreen
import ru.sokolovromann.mynotepad.screens.notes.NotesViewModel
import ru.sokolovromann.mynotepad.ui.theme.MyNotepadTheme

@ExperimentalMaterialApi
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val notesViewModel: NotesViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyNotepadTheme {
                val navController = rememberNavController()

                NavHost(navController = navController, startDestination = MainRoute.Notes.route) {
                    composable(MainRoute.Notes.route) {
                        NotesScreen(
                            notesViewModel = notesViewModel,
                            navController = navController
                        )
                    }
                    composable(MainRoute.AddNote.route) {
                        // TODO Add navigate to AddEditNoteScreen
                    }
                    composable(MainRoute.EditNote("{noteUid}").route) { navBackStackEntry ->
                        val noteUid = navBackStackEntry.arguments?.getString("noteUid")
                        // TODO Add navigate to AddEditNoteScreen
                    }
                }
            }
        }
    }
}
