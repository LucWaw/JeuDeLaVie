package kmp.project.gameoflife.ui.draganddrop

import androidx.compose.foundation.draganddrop.dragAndDropSource
import androidx.compose.foundation.draganddrop.dragAndDropTarget
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.layout
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import kmp.project.gameoflife.buildTextTransferData
import kmp.project.gameoflife.getText
import kmp.project.gameoflife.hasText
import kmp.project.gameoflife.ui.pattern.PatternUIState
import kotlin.math.min


object LocalDragDropState {
    var draggedPattern: PatternUIState? = null
}

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
        Size(tileSize.width * gridSize *2 , tileSize.height * gridSize *2) //YOU CAN MODIFY HERE
    }

    var boxSize by remember { mutableStateOf(IntSize.Zero) }

    val dragSourceModifier = remember(data, tileSize, ghostSizePx, isInDark, boxSize) {
        Modifier.dragAndDropSource(
            drawDragDecoration = {
                val currentState = data()
                if (currentState != null) {
                    val patternGridSize = currentState.gridSize

                    // Appliquer la même réduction que dans le Modifier.layout
                    val gWidth = ghostSizePx.width
                    val gHeight = ghostSizePx.height
                    // 'size' est la taille de la zone allouée au drag
                    val scale = min(size.width / gWidth, size.height / gHeight)

                    // La vraie taille d'une case, adaptée à l'écran
                    val tileW = tileSize.width * scale
                    val tileH = tileSize.height * scale

                    // Calculer le point central de la case en bas à droite
                    val bottomRightCenterX = (patternGridSize * tileW) - (tileW / 2)
                    val bottomRightCenterY = (patternGridSize * tileH) - (tileH / 2)

                    // Calculer le décalage pour que ce point soit EXACTEMENT
                    //    au milieu de la zone de drag (là où se trouve le pointeur)
                    val startX = (size.width / 2f) - bottomRightCenterX
                    val startY = (size.height / 2f) - bottomRightCenterY

                    for (i in 0 until patternGridSize) {
                        for (j in 0 until patternGridSize) {
                            // Plus de risque de négatif, on part de startX et startY !
                            val topLeft = Offset(startX + j * tileW, startY + i * tileH)
                            val rectSize = Size(tileW, tileH)

                            if (currentState.cells.contains(Pair(i, j))) {
                                drawRect(
                                    color = if (isInDark) Color.White else Color.Black,
                                    topLeft = topLeft,
                                    size = rectSize,
                                    style = Fill
                                )
                            }
                            drawRect(
                                color = Color.Gray,
                                topLeft = topLeft,
                                size = rectSize,
                                style = Stroke(width = 1.dp.toPx())
                            )
                        }
                    }
                }
            }
        ) { pointerOffset ->
            val currentState = data()
            if (currentState != null) {
                LocalDragDropState.draggedPattern = currentState

                //On calcule le centre exact de la Box pour y accrocher la souris Desktop
                val centerOffset = if (boxSize != IntSize.Zero) {
                    Offset(boxSize.width / 2f, boxSize.height / 2f)
                } else {
                    pointerOffset // Sécurité si la taille n'est pas encore calculée
                }

                buildTextTransferData("LOCAL_PATTERN", dragOffset = centerOffset)
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
            .onSizeChanged { boxSize = it }
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
                if (textReceived == "LOCAL_PATTERN") {
                    val pattern = LocalDragDropState.draggedPattern
                    if (pattern != null) {
                        onDropPattern(pattern)

                        // Clean up memory after the drop
                        LocalDragDropState.draggedPattern = null
                        return true
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