package kmp.project.gameoflife

import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draganddrop.DragAndDropEvent
import androidx.compose.ui.draganddrop.DragAndDropTransferData
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.LayoutCoordinates
import org.jetbrains.compose.resources.DrawableResource
import org.koin.core.module.Module

interface Platform {
    val name: String
    val isDynamicColorSupported: Boolean
}

expect fun getPlatform(): Platform

@Composable
expect fun GifImage(ressources: DrawableResource, modifier: Modifier = Modifier)

expect fun buildTextTransferData(text: String,dragOffset: Offset = Offset.Zero): DragAndDropTransferData
expect fun DragAndDropEvent.hasText(): Boolean
expect fun DragAndDropEvent.getText(): String?

// Added to support single board drop target
expect fun DragAndDropEvent.getPositionIn(container: LayoutCoordinates): Offset

@Composable
expect fun platformColors(useDarkTheme: Boolean): ColorScheme


expect fun platformModule(): Module

internal const val dataStoreFileName = "gameoflife-preferences.preferences_pb"