package kmp.project.gameoflife

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import kmp.project.gameoflife.ui.onboard.OnboardingUtils
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

class JVMPlatform: Platform {
    override val name: String = "Java ${System.getProperty("java.version")}"
}

actual fun getPlatform(): Platform = JVMPlatform()

actual fun getOnboardingUtils(): OnboardingUtils {
    return DesktopOnboardingUtils()
}

@Composable
actual fun GifImage(ressources: DrawableResource) {
    Image(
        painter = painterResource(ressources),
        contentDescription = null,
        modifier = Modifier.fillMaxWidth()
    )
}