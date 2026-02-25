package kmp.project.gameoflife.ui.draganddrop

import androidx.compose.foundation.draganddrop.dragAndDropSource
import androidx.compose.foundation.draganddrop.dragAndDropTarget
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draganddrop.DragAndDropEvent
import androidx.compose.ui.draganddrop.DragAndDropTarget
import kmp.project.gameoflife.buildTextTransferData
import kmp.project.gameoflife.getText
import kmp.project.gameoflife.hasText
import kmp.project.gameoflife.ui.pattern.PatternUIState
import kotlinx.serialization.json.Json

//TODO TAILLE adapté a la grille


@Composable
fun CustomDragTarget(
    data: () -> PatternUIState?,
    modifier: Modifier = Modifier,
    visual: @Composable () -> Unit,
) {
    val dragSourceModifier = remember(data) {
        Modifier.dragAndDropSource { _ ->
            val currentState = data()
            println(currentState)
            if (currentState != null) {
                buildTextTransferData(Json.encodeToString(currentState))
            } else {
                null
            }
        }
    }

    Box(
        modifier = modifier.then(dragSourceModifier),
        contentAlignment = Alignment.Center
    ) {
        visual()
    }
}


@Composable
fun CustomDropTarget(
    modifier: Modifier = Modifier,
    onDropPattern: (PatternUIState) -> Unit,
    visual: @Composable () -> Unit
) {
    val dropTarget = remember(onDropPattern) {
        object : DragAndDropTarget {
            override fun onDrop(event: DragAndDropEvent): Boolean {
                val textReceived = event.getText()
                if (textReceived != null) {
                    try {
                        val pattern = Json.decodeFromString<PatternUIState>(textReceived)
                        onDropPattern(pattern)
                        return true
                    } catch (_: Exception) {
                        return false
                    }
                }
                return false
            }
        }
    }

    val shouldStartDrag = remember { { event: DragAndDropEvent -> event.hasText() } }

    Box(
        modifier = modifier.dragAndDropTarget(
            shouldStartDragAndDrop = shouldStartDrag,
            target = dropTarget
        ),
        contentAlignment = Alignment.Center
    ) {
        visual()
    }
}