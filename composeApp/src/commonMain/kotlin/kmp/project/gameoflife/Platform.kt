package kmp.project.gameoflife

import androidx.compose.runtime.Composable
import kmp.project.gameoflife.ui.onboard.OnboardingUtils
import org.jetbrains.compose.resources.DrawableResource

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform

expect fun getOnboardingUtils(): OnboardingUtils

@Composable
expect fun GifImage(ressources: DrawableResource)