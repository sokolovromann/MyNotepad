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
import ru.sokolovromann.mynotepad.*
import ru.sokolovromann.mynotepad.R
import ru.sokolovromann.mynotepad.data.repository.SettingsRepository
import ru.sokolovromann.mynotepad.ui.theme.MyNotepadTheme
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
                    settingsGraph(
                        navController = navController,
                        onOpenGitHub = { openGitHub() },
                        onOpenEmailApp = { openEmailApp() },
                        onOpenTerms = { openTerms() },
                        onOpenPrivacyPolicy = { openPrivacyPolicy() }
                    )
                }
            }
        }
    }

    private fun openGitHub() {
        val intent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse(getString(R.string.app_github_link))
        )
        startActivity(intent)
    }

    private fun openEmailApp() {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            type = "text/plain"
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.app_developer_email)))
            putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name))
        }
        startActivity(Intent.createChooser(intent, getString(R.string.app_choose_email_app)))
    }

    private fun openTerms() {
        val intent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse(getString(R.string.app_terms_link))
        )
        startActivity(intent)
    }

    private fun openPrivacyPolicy() {
        val intent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse(getString(R.string.app_privacy_policy_link))
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
