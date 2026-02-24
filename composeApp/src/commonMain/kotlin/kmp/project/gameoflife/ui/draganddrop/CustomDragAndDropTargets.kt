package kmp.project.gameoflife.ui.draganddrop

import androidx.compose.foundation.draganddrop.dragAndDropSource
import androidx.compose.foundation.draganddrop.dragAndDropTarget
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draganddrop.DragAndDropEvent
import androidx.compose.ui.draganddrop.DragAndDropTarget
import kmp.project.gameoflife.buildTextTransferData
import kmp.project.gameoflife.getText
import kmp.project.gameoflife.hasText
import kmp.project.gameoflife.ui.pattern.PatternUIState
import kotlinx.serialization.json.Json

//TODO TAILLE ET PAS DE CLIGNOTEMENT
@Composable
fun CustomDragTarget(
    data: PatternUIState, modifier: Modifier = Modifier,
    visual: @Composable () -> Unit,
) {
    Box(
        modifier = modifier
            .dragAndDropSource { _ ->
                buildTextTransferData(Json.encodeToString(data))
            },
        contentAlignment = Alignment.Center
    ) {
        visual()
    }
}

@Composable
fun CustomDropTarget(
    modifier: Modifier = Modifier,
    visual: @Composable (Modifier, PatternUIState?) -> Unit
) {
    var patternDrop by remember { mutableStateOf<PatternUIState?>(null) }

    //var isHovered by remember { mutableStateOf(false) }

    val dropTarget = remember {
        object : DragAndDropTarget {

            override fun onDrop(event: DragAndDropEvent): Boolean {
                val textReceived = event.getText()
                if (textReceived != null) {
                    patternDrop = Json.decodeFromString<PatternUIState>(textReceived)
                    return true
                }
                return false
            }
        }
    }
    visual(
        modifier.dragAndDropTarget(
            shouldStartDragAndDrop = { event -> event.hasText() },
            target = dropTarget
        ), patternDrop
    )
}