package kmp.project.gameoflife

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import kmp.project.gameoflife.ui.onboard.OnboardingUtils
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import androidx.compose.ui.draganddrop.DragAndDropEvent
import androidx.compose.ui.draganddrop.DragAndDropTransferData
import androidx.compose.ui.draganddrop.DragAndDropTransferAction
import androidx.compose.ui.draganddrop.DragAndDropTransferable
import androidx.compose.ui.draganddrop.awtTransferable
import java.awt.datatransfer.DataFlavor
import java.awt.datatransfer.StringSelection
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
actual fun buildTextTransferData(text: String): DragAndDropTransferData {
    return DragAndDropTransferData(
        transferable = DragAndDropTransferable(
            StringSelection(text)
        ),
        supportedActions = listOf(
            DragAndDropTransferAction.Copy,
            DragAndDropTransferAction.Move,
            DragAndDropTransferAction.Link,
        )
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