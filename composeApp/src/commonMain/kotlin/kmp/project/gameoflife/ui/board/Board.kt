package kmp.project.gameoflife.ui.board

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import kmp.project.gameoflife.ui.GameUiState
import kmp.project.gameoflife.ui.draganddrop.CustomDropTarget
import kotlin.math.abs
import kotlin.math.min

/**
 * A composable that renders the Game of Life grid and handles user interactions.
 *
 * This component manages the drawing of the grid lines, alive cells, and hover highlights.
 * It supports various input methods including tapping to toggle cells, dragging to draw/erase
 * paths (using Bresenham's line algorithm for interpolation), and drag-and-drop support
 * for placing predefined patterns.
 *
 * @param modifier The [Modifier] to be applied to the board container.
 * dimensions or dynamically calculated ones.
 * @param gameUIState The current state of the game, containing the set of currently alive (colored) cells.
 * @param onCellClick Callback triggered when a cell is tapped. Returns the (row, column) coordinates.
 * @param onToggleCell Callback triggered when a cell's state should be modified.
 * Returns the (row, column) coordinates and an optional boolean for the forced state (null for toggle).
 * @param gridChange Callback triggered when the board size changes, providing the new [Size].
 */
@Composable
fun Board(
    modifier: Modifier = Modifier,
    rows: Int,
    columns: Int,
    gameUIState: GameUiState,
    onCellClick: (Pair<Int, Int>) -> Unit,
    onToggleCell: (Pair<Int, Int>, Boolean?) -> Unit = { _, _ -> },
    gridChange: (Size) -> Unit,
) {

    var lastToggledCell by remember { mutableStateOf<Pair<Int, Int>?>(null) }
    var hoverCell by remember { mutableStateOf<Pair<Int, Int>?>(null) }

    val colorPrimary = MaterialTheme.colorScheme.primary
    val colorSurface = MaterialTheme.colorScheme.surface
    val colorOutline = MaterialTheme.colorScheme.outline
    val density = LocalDensity.current

    BoxWithConstraints(modifier = modifier.fillMaxSize().background(colorSurface), contentAlignment = Alignment.Center) {
        val widthPx = constraints.maxWidth
        val heightPx = constraints.maxHeight

        val tileSize = if (columns > 0 && rows > 0) {
            min(widthPx / columns, heightPx / rows)
        } else 0

        if (tileSize > 0) {
            val gridWidth = tileSize * columns
            val gridHeight = tileSize * rows



            CustomDropTarget(
                modifier = Modifier
                    .size(
                        width = with(density) { gridWidth.toDp() },
                        height = with(density) { gridHeight.toDp() }
                    )
                    .onSizeChanged { gridChange(it.toSize()) },
                onDropPattern = { pattern, dropOffset ->
                    val rawDropRow = dropOffset.y.toInt() / tileSize
                    val rawDropCol = dropOffset.x.toInt() / tileSize

                    val rowOffset = pattern.gridSize - 1
                    val colOffset = pattern.gridSize - 1

                    pattern.cells.forEach { patternCell ->
                        val targetRow = patternCell.first + rawDropRow - rowOffset
                        val targetCol = patternCell.second + rawDropCol - colOffset

                        if (targetRow in 0 until rows && targetCol in 0 until columns) {
                            onToggleCell(Pair(targetRow, targetCol), true)
                        }
                    }
                }
            ) {
                Canvas(
                    modifier = Modifier
                        .fillMaxSize()
                        .pointerInput(tileSize, rows, columns) {
                            detectTapGestures { offset ->
                                val cell =
                                    cellCoordinatesAtOffset(offset, tileSize, rows, columns)
                                if (cell != null) onCellClick(cell)
                            }
                        }
                        .pointerInput(tileSize, rows, columns) {
                            detectDragGestures(
                                onDragStart = { offset ->
                                    val cell = cellCoordinatesAtOffset(
                                        offset,
                                        tileSize,
                                        rows,
                                        columns
                                    )
                                    if (cell != null) {
                                        onToggleCell(cell, null)
                                        lastToggledCell = cell
                                    }
                                    hoverCell = null
                                },
                                onDrag = { change, _ ->
                                    val cell = cellCoordinatesAtOffset(
                                        change.position,
                                        tileSize,
                                        rows,
                                        columns
                                    )
                                    if (cell != null && cell != lastToggledCell) {
                                        interpolateCells(
                                            lastToggledCell ?: cell,
                                            cell
                                        ).forEach { interpolatedCell ->
                                            if (interpolatedCell != lastToggledCell) {
                                                onToggleCell(interpolatedCell, null)
                                            }
                                        }
                                        lastToggledCell = cell
                                    }
                                },
                                onDragEnd = { lastToggledCell = null; hoverCell = null },
                                onDragCancel = { lastToggledCell = null; hoverCell = null }
                            )
                        }
                        .pointerInput(tileSize, rows, columns) {
                            awaitPointerEventScope {
                                while (true) {
                                    val event = awaitPointerEvent()
                                    val position = event.changes.first().position
                                    when (event.type) {
                                        PointerEventType.Move, PointerEventType.Enter -> {
                                            hoverCell = cellCoordinatesAtOffset(
                                                position,
                                                tileSize,
                                                rows,
                                                columns
                                            )
                                        }

                                        PointerEventType.Exit, PointerEventType.Release -> {
                                            hoverCell = null
                                        }
                                    }
                                }
                            }
                        }
                ) {
                    val strokeWidthPx = 0.5.dp.toPx()

                    gameUIState.colored.forEach { (row, col) ->
                        if (row in 0 until rows && col in 0 until columns) {
                            drawRect(
                                color = colorPrimary,
                                topLeft = Offset(
                                    (col * tileSize).toFloat(),
                                    (row * tileSize).toFloat()
                                ),
                                size = Size(tileSize.toFloat(), tileSize.toFloat()),
                                style = Fill
                            )
                        }
                    }

                    hoverCell?.let { cell ->
                        if (!gameUIState.colored.contains(cell)) {
                            drawRect(
                                color = colorPrimary.copy(alpha = 0.3f),
                                topLeft = Offset(
                                    (cell.second * tileSize).toFloat(),
                                    (cell.first * tileSize).toFloat()
                                ),
                                size = Size(tileSize.toFloat(), tileSize.toFloat()),
                                style = Fill
                            )
                        }
                    }

                    for (i in 0..columns) {
                        val x = (i * tileSize).toFloat()
                        drawLine(
                            color = colorOutline,
                            start = Offset(x, 0f),
                            end = Offset(x, gridHeight.toFloat()),
                            strokeWidth = strokeWidthPx
                        )
                    }
                    for (i in 0..rows) {
                        val y = (i * tileSize).toFloat()
                        drawLine(
                            color = colorOutline,
                            start = Offset(0f, y),
                            end = Offset(gridWidth.toFloat(), y),
                            strokeWidth = strokeWidthPx
                        )
                    }
                }
            }

        }
    }
}

fun cellCoordinatesAtOffset(
    hitPoint: Offset,
    tileSize: Int,
    gridRow: Int,
    gridColumn: Int
): Pair<Int, Int>? {
    if (tileSize <= 0) return null
    val col = hitPoint.x.toInt() / tileSize
    val row = hitPoint.y.toInt() / tileSize
    if (col in 0 until gridColumn && row in 0 until gridRow) {
        return Pair(row, col)
    }
    return null
}

fun interpolateCells(start: Pair<Int, Int>, end: Pair<Int, Int>): List<Pair<Int, Int>> {
    val result = mutableListOf<Pair<Int, Int>>()
    var x0 = start.second
    var y0 = start.first
    val x1 = end.second
    val y1 = end.first
    val dx = abs(x1 - x0)
    val dy = abs(y1 - y0)
    val sx = if (x0 < x1) 1 else -1
    val sy = if (y0 < y1) 1 else -1
    var err = dx - dy
    while (true) {
        result.add(Pair(y0, x0))
        if (x0 == x1 && y0 == y1) break
        val e2 = 2 * err
        if (e2 > -dy) {
            err -= dy
            x0 += sx
        }
        if (e2 < dx) {
            err += dx
            y0 += sy
        }
    }
    return result
}
