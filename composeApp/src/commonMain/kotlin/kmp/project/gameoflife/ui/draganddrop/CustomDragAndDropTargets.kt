package kmp.project.gameoflife.ui.draganddrop

import androidx.compose.foundation.draganddrop.dragAndDropSource
import androidx.compose.foundation.draganddrop.dragAndDropTarget
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draganddrop.DragAndDropEvent
import androidx.compose.ui.draganddrop.DragAndDropTarget
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.layout
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import kmp.project.gameoflife.buildTextTransferData
import kmp.project.gameoflife.getText
import kmp.project.gameoflife.hasText
import kmp.project.gameoflife.ui.pattern.PatternUIState
import kotlinx.serialization.json.Json
import kotlin.math.min


@Composable
fun CustomDragTarget(
    data: () -> PatternUIState?,
    modifier: Modifier = Modifier,
    gridSize: Int = 1,
    tileSize: Size = Size(20f, 20f),
    visual: @Composable () -> Unit,
) {
    val isInDark = isSystemInDarkTheme()

    val ghostSizePx = remember(gridSize, tileSize) {
        Size(tileSize.width * gridSize, tileSize.height * gridSize)
    }

    val dragSourceModifier = remember(data, tileSize, isInDark) {
        Modifier.dragAndDropSource(
            drawDragDecoration = {
                val currentState = data()
                if (currentState != null) {
                    val patternGridSize = currentState.gridSize
                    val tileW = tileSize.width
                    val tileH = tileSize.height

                    for (i in 0 until patternGridSize) {
                        for (j in 0 until patternGridSize) {
                            val topLeft = Offset(j * tileW, i * tileH)
                            val size = Size(tileW, tileH)

                            if (currentState.cells.contains(Pair(i, j))) {
                                drawRect(
                                    color = if (isInDark) Color.White else Color.Black,
                                    topLeft = topLeft,
                                    size = size,
                                    style = Fill
                                )
                            }
                            drawRect(
                                color = Color.Gray,
                                topLeft = topLeft,
                                size = size,
                                style = Stroke(width = 1.dp.toPx())
                            )
                        }
                    }
                }
            }
        ) { _ ->
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
        modifier = modifier
            .layout { measurable, constraints ->
                val width = constraints.maxWidth
                val height = constraints.maxHeight

                val gWidth = ghostSizePx.width.toInt().coerceAtLeast(1)
                val gHeight = ghostSizePx.height.toInt().coerceAtLeast(1)

                val placeable = measurable.measure(
                    Constraints.fixed(gWidth, gHeight)
                )

                val scale = min(width.toFloat() / gWidth, height.toFloat() / gHeight)

                layout(width, height) {
                    placeable.placeWithLayer(
                        (width - gWidth) / 2,
                        (height - gHeight) / 2
                    ) {
                        scaleX = scale
                        scaleY = scale
                    }
                }
            }
            .then(dragSourceModifier),
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