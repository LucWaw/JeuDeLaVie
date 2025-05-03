package kmp.project.gameoflife

import kmp.project.gameoflife.ui.onboard.OnboardingUtils
import java.io.File

class DesktopOnboardingUtils : OnboardingUtils {

    private val file: File = File(System.getProperty("user.home"), ".onboarding_prefs")

    override fun isOnboardingCompleted(): Boolean {
        return file.exists() && file.readText().trim() == "completed=true"
    }

    override fun setOnboardingCompleted() {
        file.writeText("completed=true")
    }
}