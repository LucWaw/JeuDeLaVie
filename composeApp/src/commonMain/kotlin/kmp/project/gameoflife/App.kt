package kmp.project.gameoflife

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import kmp.project.gameoflife.ui.GameOfLife
import kmp.project.gameoflife.ui.draganddrop.DragAndDropTheme
import kmp.project.gameoflife.ui.onboard.OnboardingScreen

@Composable
fun App(isTablet: Boolean = false) {
    val onboardingUtils = getOnboardingUtils()
    var showOnboarding by rememberSaveable { mutableStateOf(!onboardingUtils.isOnboardingCompleted()) }
    DragAndDropTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {
            if (showOnboarding) {
                OnboardingScreen(
                    onFinished = {
                        onboardingUtils.setOnboardingCompleted()
                        showOnboarding = false
                    }
                )
            } else {
                GameOfLife(isTablet = isTablet, showOnboarding = {
                    showOnboarding = true
                }, modifier = Modifier.windowInsetsPadding(WindowInsets.navigationBars))
            }
        }
    }
}

