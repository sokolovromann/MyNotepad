package ru.sokolovromann.mynotepad.screens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import ru.sokolovromann.mynotepad.screens.addeditnote.AddEditNoteScreen
import ru.sokolovromann.mynotepad.screens.notes.NotesScreen
import ru.sokolovromann.mynotepad.screens.notes.NotesViewModel
import ru.sokolovromann.mynotepad.ui.theme.MyNotepadTheme

@ExperimentalFoundationApi
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
                        AddEditNoteScreen(navController = navController)
                    }
                    composable(MainRoute.EditNote("{uid}").route) {
                        AddEditNoteScreen(navController = navController)
                    }
                }
            }
        }
    }
}
