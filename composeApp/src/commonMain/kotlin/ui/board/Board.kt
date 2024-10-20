package ui.board

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import kotlinx.coroutines.flow.StateFlow
import ui.GameUiState
import ui.draganddrop.DropTarget

var placingCell: Pair<Int, Int>? = null
var bundledCells: List<Pair<Int, Int>>? = null
var activated = false

@Composable
fun Board(
    gameUIState: GameUiState, onCellClick: (Pair<Int, Int>) -> Unit,
    gridUiSize: StateFlow<Size>,
    gridChange: (Size) -> Unit,
    modifier: Modifier = Modifier
) {

    val boardUiState by gridUiSize.collectAsState()


    val scroll = rememberLazyGridState()

    var currentPosition by mutableStateOf(Offset.Zero)

    var currentCellCoordinates = Pair(0, 0)
    val changeCurentCellCoordinates = { coordinate: Pair<Int, Int> ->
        currentCellCoordinates = coordinate
    }
    LazyVerticalGrid(
        GridCells.Fixed(gameUIState.gridSize),
        state = scroll,
        modifier = Modifier
            .onGloballyPositioned {
                currentPosition = it.localToWindow(Offset.Zero)
            }
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = { offset ->
                        dragStart(
                            offset,
                            gameUIState,
                            boardUiState,
                            changeCurentCellCoordinates,
                            onCellClick
                        )
                    },
                    onDrag = { change, _ ->
                        drag(
                            change,
                            boardUiState,
                            gameUIState.gridSize,
                            currentCellCoordinates,
                            changeCurentCellCoordinates,
                            onCellClick
                        )
                    }
                )
            }
            .pointerInput(Unit) {
                detectDragGesturesAfterLongPress(
                    onDragStart = { offset ->
                        dragStart(
                            offset,
                            gameUIState,
                            boardUiState,
                            changeCurentCellCoordinates,
                            onCellClick
                        )
                    },
                    onDrag = { change, _ ->
                        drag(
                            change,
                            boardUiState,
                            gameUIState.gridSize,
                            currentCellCoordinates,
                            changeCurentCellCoordinates,
                            onCellClick
                        )
                    }
                )
            }
            .onSizeChanged { newSize ->
                gridChange(newSize.toSize())
            }
    ) {
        items(gameUIState.gridSize * gameUIState.gridSize) { index ->
            val cellCoordinates = Pair(index / gameUIState.gridSize, index % gameUIState.gridSize)
            val interactionSource = remember { MutableInteractionSource() }

            DropTarget(modifier = modifier) { isInBound, bundleOfCells ->
                if (isInBound && bundleOfCells != null) {
                    placingCell = cellCoordinates
                    bundledCells = bundleOfCells.cells
                    activated = true
                }
                if (activated) {
                    activated = false

                    bundledCells?.forEach { patternCell ->
                        if (placingCell != null) {
                            onCellClick(
                                Pair(
                                    patternCell.first + (placingCell?.first ?: 0),
                                    patternCell.second + (placingCell?.second ?: 0)
                                )
                            )
                        }

                    }
                }
                val isInDark = isSystemInDarkTheme()



                Box(
                    modifier = Modifier
                        .aspectRatio(1f)
                        .background(
                            if (gameUIState.colored.contains(cellCoordinates) || interactionSource.collectIsHoveredAsState().value)
                                if (isInDark) {
                                    Color.White
                                } else {
                                    Color.Black
                                }
                            else Color.Transparent
                        )
                        .border(1.dp, Color.Gray)
                        .clickable { onCellClick(cellCoordinates) }
                        .hoverable(interactionSource = interactionSource)
                )
            }
        }
    }
}

private fun drag(
    change: PointerInputChange,
    gridSize: Size,
    cellsGridSize: Int,
    currentCellCoordinates: Pair<Int, Int>,
    changeCurentCellCoordinates: (Pair<Int, Int>) -> Unit,
    onCellClick: (Pair<Int, Int>) -> Unit
) {
    cellCoordinatesAtOffset(
        change.position,
        gridSize,
        cellsGridSize
    ).let { pointerCellCoordinates ->
        if (currentCellCoordinates != pointerCellCoordinates) {
            onCellClick(pointerCellCoordinates)
            changeCurentCellCoordinates(pointerCellCoordinates)
        }
    }
}

private fun dragStart(
    offset: Offset,
    gameUIState: GameUiState,
    gridSize: Size,
    changeCurentCellCoordinates: (Pair<Int, Int>) -> Unit,
    onCellClick: (Pair<Int, Int>) -> Unit
) {
    cellCoordinatesAtOffset(offset, gridSize, gameUIState.gridSize).let { pair ->
        if (!gameUIState.colored.contains(pair)) {
            changeCurentCellCoordinates(pair)
            onCellClick(pair)
        }
    }
}

fun cellCoordinatesAtOffset(
    hitPoint: Offset,
    uiGridSize: Size,
    cellsGridSize: Int
): Pair<Int, Int> {
    // Calculate the actual size of each cell
    val tileSize = uiGridSize.width / cellsGridSize
    val x = (hitPoint.x / tileSize).toInt()
    val y = (hitPoint.y / tileSize).toInt()
    return Pair(y, x)
}