package kmp.project.gameoflife

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draganddrop.DragAndDropEvent
import androidx.compose.ui.draganddrop.DragAndDropTransferAction
import androidx.compose.ui.draganddrop.DragAndDropTransferData
import androidx.compose.ui.draganddrop.DragAndDropTransferable
import androidx.compose.ui.draganddrop.awtTransferable
import androidx.compose.ui.geometry.Offset
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kmp.project.gameoflife.data.GameOfLifeDatabase
import kmp.project.gameoflife.ui.onboard.OnboardingUtils
import kmp.project.gameoflife.ui.theme.DarkColorScheme
import kmp.project.gameoflife.ui.theme.LightColorScheme
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import java.awt.datatransfer.DataFlavor
import java.awt.datatransfer.StringSelection
import java.io.File

class JVMPlatform: Platform {
    override val name: String = "Java ${System.getProperty("java.version")}"
}

actual fun getPlatform(): Platform = JVMPlatform()

actual fun getOnboardingUtils(): OnboardingUtils {
    return DesktopOnboardingUtils()
}

@Composable
actual fun GifImage(ressources: DrawableResource, modifier: Modifier) {
    Image(
        painter = painterResource(ressources),
        contentDescription = null,
        modifier = modifier.fillMaxWidth()
    )
}



// Build data using Desktop's StringSelection and Transferable wrappers
@OptIn(ExperimentalComposeUiApi::class)
actual fun buildTextTransferData(text: String, dragOffset: Offset): DragAndDropTransferData {
    return DragAndDropTransferData(
        transferable = DragAndDropTransferable(StringSelection(text)),
        supportedActions = listOf(
            DragAndDropTransferAction.Copy,
            DragAndDropTransferAction.Move,
            DragAndDropTransferAction.Link,
        ),
        dragDecorationOffset = dragOffset
    )
}


// Check if AWT payload contains a string
@OptIn(ExperimentalComposeUiApi::class)
actual fun DragAndDropEvent.hasText(): Boolean {
    return this.awtTransferable.isDataFlavorSupported(DataFlavor.stringFlavor)
}

// Extract the string using AWT DataFlavors
@OptIn(ExperimentalComposeUiApi::class)
actual fun DragAndDropEvent.getText(): String? {
    val transferable = this.awtTransferable
    return if (transferable.isDataFlavorSupported(DataFlavor.stringFlavor)) {
        transferable.getTransferData(DataFlavor.stringFlavor) as? String
    } else {
        null
    }
}

actual fun showToast(message: String) {
    // No-op for desktop as requested
}

actual fun getDatabaseBuilder(): RoomDatabase.Builder<GameOfLifeDatabase> {
    val dbFile = File(System.getProperty("java.io.tmpdir"), GameOfLifeDatabase.DB_NAME)
    return Room.databaseBuilder<GameOfLifeDatabase>(
        name = dbFile.absolutePath,
    ).setDriver(BundledSQLiteDriver())
}

@Composable
actual fun platformColors(
    useDarkTheme: Boolean
): ColorScheme {
    return if (useDarkTheme) DarkColorScheme else LightColorScheme
}