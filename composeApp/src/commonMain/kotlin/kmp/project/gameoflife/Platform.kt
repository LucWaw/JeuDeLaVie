package kmp.project.gameoflife

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draganddrop.DragAndDropEvent
import androidx.compose.ui.draganddrop.DragAndDropTransferData
import androidx.compose.ui.geometry.Offset
import kmp.project.gameoflife.ui.onboard.OnboardingUtils
import org.jetbrains.compose.resources.DrawableResource

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform

expect fun getOnboardingUtils(): OnboardingUtils

@Composable
expect fun GifImage(ressources: DrawableResource, modifier: Modifier = Modifier)

expect fun buildTextTransferData(text: String,dragOffset: Offset = Offset.Zero): DragAndDropTransferData
expect fun DragAndDropEvent.hasText(): Boolean
expect fun DragAndDropEvent.getText(): String?
