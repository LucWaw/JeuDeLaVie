package kmp.project.gameoflife

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kmp.project.gameoflife.ui.GameOfLife
import kmp.project.gameoflife.ui.theme.DragAndDropTheme
import kmp.project.gameoflife.ui.onboard.OnboardingScreen
import kmp.project.gameoflife.ui.settings.Settings
import kmp.project.gameoflife.ui.theme.ThemeViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun App(isTablet: Boolean = false) {
    val onboardingUtils = getOnboardingUtils()
    var showOnboarding by rememberSaveable { mutableStateOf(!onboardingUtils.isOnboardingCompleted()) }

    val navController = rememberNavController()
    val themeViewModel: ThemeViewModel = koinViewModel()
    val theme by themeViewModel.themeState.collectAsState()

    DragAndDropTheme(theme = theme) {

        NavHost(navController, startDestination = Game) {
            composable<Game> {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    if (showOnboarding) {
                        OnboardingScreen(
                            onFinished = {
                                onboardingUtils.setOnboardingCompleted()
                                showOnboarding = false
                            }
                        )
                    } else {
                        GameOfLife(
                            isTablet = isTablet, showOnboarding = {
                                showOnboarding = true
                            }, modifier = Modifier.windowInsetsPadding(WindowInsets.navigationBars),
                            goToSettings = { navController.navigate(Settings) }
                        )
                    }
                }
            }
            composable<Settings> {
                Settings(goBack = { navController.popBackStack() })
            }
        }
    }
}
