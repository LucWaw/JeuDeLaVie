package kmp.project.gameoflife.ui.draganddrop

import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.toSize
import kmp.project.gameoflife.getPlatform
import kmp.project.gameoflife.ui.pattern.PatternUIState
import kotlinx.coroutines.flow.StateFlow


@Composable
fun LongPressDraggable(
    modifier: Modifier = Modifier,
    sizeUi: StateFlow<Size>,
    size: Int,
    state: DragTargetInfo,
    content: @Composable BoxScope.() -> Unit
) {
    val sizeUiState by sizeUi.collectAsState()
    val tileSize = sizeUiState.width / size

    val isDesktop: Boolean = getPlatform().name.startsWith("Java")

    Box(modifier = modifier)
    {
        content()
        if (state.isDragging) {
            var targetSize by remember {
                mutableStateOf(IntSize.Zero)
            }
            Box(modifier = Modifier
                .graphicsLayer {
                    val offset = (state.dragPosition + state.dragOffset)
                    val patternSize = (state.dataToDrop as PatternUIState).gridSize



// Modifier le placement en fonction de la plateforme
                    scaleX = (tileSize * patternSize / targetSize.toSize().width)
                    scaleY = (tileSize * patternSize / targetSize.toSize().height)
                    alpha = if (targetSize == IntSize.Zero) 0f else .9f
                    translationX = offset.x - (targetSize.width / 2) + (tileSize * (patternSize - 1) / 2)

// Appliquer une translation différente pour mobile et desktop
                    translationY = if (isDesktop) {
                        offset.y - (targetSize.height / 2) + (tileSize * (patternSize - 1) / 2)
                    } else {
                        // Décaler vers le haut d'une tile complète sur mobile
                        offset.y - (targetSize.height / 2) + (tileSize * (patternSize - 1) / 2) - tileSize
                    }




                }
                .onGloballyPositioned {
                    targetSize = it.size
                }
            ) {
                state.draggableComposable?.invoke()
            }
        }
    }

}

@Composable
fun DragTarget(
    modifier: Modifier = Modifier,
    dataToDrop: () -> PatternUIState?,
    localDragTargetInfo : DragTargetInfo,
    content: @Composable (() -> Unit)
) {

    var currentPosition by remember { mutableStateOf(Offset.Zero) }

    val isDesktop: Boolean = getPlatform().name.startsWith("Java")
    Box(modifier = modifier
        .onGloballyPositioned {
            currentPosition = it.localToWindow(Offset.Zero)
        }
        .pointerInput(Unit) {
            if (isDesktop) {
                detectDragGestures(onDragStart = {
                    localDragTargetInfo.dataToDrop = dataToDrop()
                    localDragTargetInfo.isDragging = true
                    localDragTargetInfo.dragPosition = currentPosition + it
                    localDragTargetInfo.draggableComposable = content
                }, onDrag = { change, dragAmount ->
                    change.consume()
                    localDragTargetInfo.dragOffset += Offset(dragAmount.x, dragAmount.y)
                }, onDragEnd = {
                    localDragTargetInfo.isDragging = false
                    localDragTargetInfo.dragOffset = Offset.Zero

                }, onDragCancel = {
                    localDragTargetInfo.dragOffset = Offset.Zero
                    localDragTargetInfo.isDragging = false
                })
            }
        }
        .pointerInput(Unit) {
            detectDragGesturesAfterLongPress(onDragStart = {
                localDragTargetInfo.dataToDrop = dataToDrop()
                localDragTargetInfo.isDragging = true
                localDragTargetInfo.dragPosition = currentPosition + it
                localDragTargetInfo.draggableComposable = content
            }, onDrag = { change, dragAmount ->
                change.consume()
                localDragTargetInfo.dragOffset += Offset(dragAmount.x, dragAmount.y)
            }, onDragEnd = {
                localDragTargetInfo.isDragging = false
                localDragTargetInfo.dragOffset = Offset.Zero

            }, onDragCancel = {
                localDragTargetInfo.dragOffset = Offset.Zero
                localDragTargetInfo.isDragging = false
            })
        }


    ) {
        content()
    }
}

@Composable
fun DropTarget(
    modifier: Modifier,
    favoriteDragTargetInfo: DragTargetInfo,
    sheetDragTargetInfo: DragTargetInfo,
    content: @Composable (BoxScope.(isInBound: Boolean, data: PatternUIState?) -> Unit)
) {

    @Composable
    fun DetectDropTarget(dragTargetInfo: DragTargetInfo) {
        val dragPosition = dragTargetInfo.dragPosition
        val dragOffset = dragTargetInfo.dragOffset
        var isCurrentDropTarget by remember { mutableStateOf(false) }

        Box(modifier = modifier.onGloballyPositioned {
            it.boundsInWindow().let { rect ->
                isCurrentDropTarget = rect.contains(dragPosition + dragOffset)
            }
        }) {
            val data = if (isCurrentDropTarget && !dragTargetInfo.isDragging) {
                dragTargetInfo.dataToDrop as? PatternUIState
            } else {
                null
            }
            content(isCurrentDropTarget, data)
        }
    }

    Box {
        DetectDropTarget(favoriteDragTargetInfo)
        DetectDropTarget(sheetDragTargetInfo)
    }
}



class DragTargetInfo {
    var isDragging: Boolean by mutableStateOf(false)
    var dragPosition by mutableStateOf(Offset.Zero)
    var dragOffset by mutableStateOf(Offset.Zero)
    var draggableComposable by mutableStateOf<(@Composable () -> Unit)?>(null)
    var dataToDrop by mutableStateOf<Any?>(null)
}