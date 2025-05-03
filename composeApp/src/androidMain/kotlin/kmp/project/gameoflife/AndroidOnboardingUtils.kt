package kmp.project.gameoflife

import android.content.Context
import androidx.core.content.edit
import kmp.project.gameoflife.ui.onboard.OnboardingUtils

class AndroidOnboardingUtils(private val context: Context) : OnboardingUtils {

    private val prefs by lazy {
        context.getSharedPreferences("onboarding", Context.MODE_PRIVATE)
    }

    override fun isOnboardingCompleted(): Boolean {
        return prefs.getBoolean("completed", false)
    }

    override fun setOnboardingCompleted() {
        prefs.edit() { putBoolean("completed", true) }
    }
}
