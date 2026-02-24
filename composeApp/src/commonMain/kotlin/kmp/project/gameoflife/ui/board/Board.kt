package kmp.project.gameoflife.ui.board

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
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import kmp.project.gameoflife.ui.GameUiState
import kmp.project.gameoflife.ui.draganddrop.CustomDropTarget
import kmp.project.gameoflife.ui.getGridColumn
import kmp.project.gameoflife.ui.getGridRow
import kotlinx.coroutines.flow.StateFlow

var placingCell: Pair<Int, Int>? = null
var bundledCells: List<Pair<Int, Int>>? = null
var activated = false

@Composable
fun Board(
    modifier: Modifier = Modifier,
    isTablet: Boolean = false,
    gameUIState: GameUiState,
    onCellClick: (Pair<Int, Int>) -> Unit,
    gridUiSize: StateFlow<Size>,
    gridChange: (Size) -> Unit,
) {

    val boardUiState by gridUiSize.collectAsState()


    val scroll = rememberLazyGridState()


    var currentCellCoordinates = Pair(0, 0)
    val changeCurentCellCoordinates = { coordinate: Pair<Int, Int> ->
        currentCellCoordinates = coordinate
    }

    val gridRow = if (isTablet) 20 else getGridRow()
    val gridColumn = if (isTablet) 80 else getGridColumn()
    val numberOfCells = gridRow * gridColumn

    LazyVerticalGrid(
        GridCells.Fixed(gridColumn),
        state = scroll,
        modifier = Modifier
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = { offset ->
                        dragStart(
                            offset,
                            gameUIState,
                            gridRow,
                            gridColumn,
                            boardUiState,
                            changeCurentCellCoordinates,
                            onCellClick
                        )
                    },
                    onDrag = { change, _ ->
                        drag(
                            change,
                            boardUiState,
                            gridRow,
                            gridColumn,
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
                            gridRow,
                            gridColumn,
                            boardUiState,
                            changeCurentCellCoordinates,
                            onCellClick
                        )
                    },
                    onDrag = { change, _ ->
                        drag(
                            change,
                            boardUiState,
                            gridRow,
                            gridColumn,
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
        items(numberOfCells) { index ->
            val cellCoordinates = Pair(index / gridColumn, index % gridColumn)

            val interactionSource = remember { MutableInteractionSource() }

            CustomDropTarget() { modifier, data ->
                if (data != null) {
                    placingCell = cellCoordinates
                    bundledCells = data.cells
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
                    modifier = modifier
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
                        .semantics {
                            contentDescription = "Item $index at $cellCoordinates"
                        }
                )
            }

            /*DropTarget(modifier = modifier) { isInBound, bundleOfCells ->
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
                        .semantics {
                            contentDescription = "Item $index at $cellCoordinates"
                        }
                )
            }*/
        }
    }
}

private fun drag(
    change: PointerInputChange,
    gridSize: Size,
    gridRow: Int,
    gridColumn: Int,
    currentCellCoordinates: Pair<Int, Int>,
    changeCurentCellCoordinates: (Pair<Int, Int>) -> Unit,
    onCellClick: (Pair<Int, Int>) -> Unit
) {
    // Utiliser gridRow et gridColumn pour calculer les coordonnées de la cellule
    cellCoordinatesAtOffset(
        change.position,
        gridSize,
        gridRow,
        gridColumn
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
    gridRow: Int,
    gridColumn: Int,
    gridSize: Size,
    changeCurentCellCoordinates: (Pair<Int, Int>) -> Unit,
    onCellClick: (Pair<Int, Int>) -> Unit
) {
    cellCoordinatesAtOffset(offset, gridSize, gridRow, gridColumn).let { pair ->
        if (!gameUIState.colored.contains(pair)) {
            changeCurentCellCoordinates(pair)
            onCellClick(pair)
        }
    }
}

fun cellCoordinatesAtOffset(
    hitPoint: Offset,
    uiGridSize: Size,
    gridRow: Int,
    gridColumn: Int
): Pair<Int, Int> {
    // Taille d'une cellule (en largeur et en hauteur)
    val tileWidth = uiGridSize.width / gridColumn
    val tileHeight = uiGridSize.height / gridRow

    // Calcul des coordonnées basées sur la position
    val x = (hitPoint.x / tileWidth).toInt()
    val y = (hitPoint.y / tileHeight).toInt()

    return Pair(y, x) // Retourne (ligne, colonne)
}
