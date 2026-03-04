package kmp.project.gameoflife

import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draganddrop.DragAndDropEvent
import androidx.compose.ui.draganddrop.DragAndDropTransferData
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.room.RoomDatabase
import kmp.project.gameoflife.data.GameOfLifeDatabase
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

// Added to support single board drop target
expect fun DragAndDropEvent.getPositionIn(container: LayoutCoordinates): Offset

/**
 * Interface for platform-specific toasts
 */
expect fun showToast(message: String)

expect fun getDatabaseBuilder(): RoomDatabase.Builder<GameOfLifeDatabase>

@Composable
expect fun platformColors(useDarkTheme: Boolean): ColorScheme
