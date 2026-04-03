package kmp.project.gameoflife.ui.board

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
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
import kmp.project.gameoflife.ui.getGridColumn
import kmp.project.gameoflife.ui.getGridRow
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
 * @param isTablet A boolean flag to determine if the grid should use a fixed tablet-optimized
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
    isTablet: Boolean = false,
    gameUIState: GameUiState,
    onCellClick: (Pair<Int, Int>) -> Unit,
    onToggleCell: (Pair<Int, Int>, Boolean?) -> Unit = { _, _ -> },
    gridChange: (Size) -> Unit,
) {
    val gridRow = if (isTablet) 20 else getGridRow()
    val gridColumn = if (isTablet) 80 else getGridColumn()

    var lastToggledCell by remember { mutableStateOf<Pair<Int, Int>?>(null) }
    var hoverCell by remember { mutableStateOf<Pair<Int, Int>?>(null) }
    
    val colorPrimary = MaterialTheme.colorScheme.primary
    val colorSurface = MaterialTheme.colorScheme.surface
    val colorOutline = MaterialTheme.colorScheme.outline
    val density = LocalDensity.current

    BoxWithConstraints(modifier = modifier.fillMaxSize().background(colorSurface)) {
        val availableWidth = with(density) { maxWidth.toPx() }
        val availableHeight = with(density) { maxHeight.toPx() }
        
        val tileSize = min(availableWidth / gridColumn, availableHeight / gridRow)
        val gridWidth = tileSize * gridColumn
        val gridHeight = tileSize * gridRow
        val offsetX = (availableWidth - gridWidth) / 2
        val offsetY = (availableHeight - gridHeight) / 2

        CustomDropTarget(
            modifier = Modifier
                .fillMaxSize()
                .onSizeChanged { gridChange(it.toSize()) },
            onDropPattern = { pattern, dropOffset ->
                // Ensure dropOffset is relative to the grid start
                val relativeX = dropOffset.x - offsetX
                val relativeY = dropOffset.y - offsetY
                
                val dropRow = (relativeY / tileSize).toInt().coerceIn(0, gridRow - 1)
                val dropCol = (relativeX / tileSize).toInt().coerceIn(0, gridColumn - 1)
                
                val patternOffset = pattern.gridSize - 1
                pattern.cells.forEach { patternCell ->
                    val targetRow = patternCell.first + dropRow - patternOffset
                    val targetCol = patternCell.second + dropCol - patternOffset
                    if (targetRow in 0 until gridRow && targetCol in 0 until gridColumn) {
                        onToggleCell(Pair(targetRow, targetCol), true)
                    }
                }
            }
        ) {
            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(tileSize, gridRow, gridColumn, offsetX, offsetY) {
                        detectTapGestures { offset ->
                            val cell = cellCoordinatesAtOffset(offset, tileSize, gridRow, gridColumn, offsetX, offsetY)
                            if (cell != null) onCellClick(cell)
                        }
                    }
                    .pointerInput(tileSize, gridRow, gridColumn, offsetX, offsetY) {
                        detectDragGestures(
                            onDragStart = { offset ->
                                val cell = cellCoordinatesAtOffset(offset, tileSize, gridRow, gridColumn, offsetX, offsetY)
                                if (cell != null) {
                                    onToggleCell(cell, null) // Invert state
                                    lastToggledCell = cell
                                }
                                hoverCell = null // Clear hover on drag start
                            },
                            onDrag = { change, _ ->
                                val cell = cellCoordinatesAtOffset(change.position, tileSize, gridRow, gridColumn, offsetX, offsetY)
                                if (cell != null && cell != lastToggledCell) {
                                    interpolateCells(lastToggledCell ?: cell, cell).forEach { interpolatedCell ->
                                        if (interpolatedCell != lastToggledCell) {
                                            onToggleCell(interpolatedCell, null) // Invert state
                                        }
                                    }
                                    lastToggledCell = cell
                                }
                            },
                            onDragEnd = { 
                                lastToggledCell = null
                                hoverCell = null // Fix ghosting
                            },
                            onDragCancel = { 
                                lastToggledCell = null
                                hoverCell = null // Fix ghosting
                            }
                        )
                    }
                    .pointerInput(tileSize, gridRow, gridColumn, offsetX, offsetY) {
                        awaitPointerEventScope {
                            while (true) {
                                val event = awaitPointerEvent()
                                val position = event.changes.first().position
                                
                                when (event.type) {
                                    PointerEventType.Move, PointerEventType.Enter -> {
                                        hoverCell = cellCoordinatesAtOffset(position, tileSize, gridRow, gridColumn, offsetX, offsetY)
                                    }
                                    PointerEventType.Exit, PointerEventType.Release -> {
                                        hoverCell = null // Fix ghosting
                                    }
                                }
                            }
                        }
                    }
            ) {
                if (tileSize <= 0f) return@Canvas
                
                val strokeWidthPx = 0.5.dp.toPx()

                // Draw alive cells
                gameUIState.colored.forEach { (row, col) ->
                    if (row in 0 until gridRow && col in 0 until gridColumn) {
                        drawRect(
                            color = colorPrimary,
                            topLeft = Offset(offsetX + col * tileSize, offsetY + row * tileSize),
                            size = Size(tileSize, tileSize),
                            style = Fill
                        )
                    }
                }

                // Draw hover highlight
                hoverCell?.let { cell ->
                    if (!gameUIState.colored.contains(cell)) {
                        drawRect(
                            color = colorPrimary.copy(alpha = 0.3f),
                            topLeft = Offset(offsetX + cell.second * tileSize, offsetY + cell.first * tileSize),
                            size = Size(tileSize, tileSize),
                            style = Fill
                        )
                    }
                }

                // Draw grid lines
                for (i in 0..gridColumn) {
                    val x = offsetX + i * tileSize
                    drawLine(
                        color = colorOutline,
                        start = Offset(x, offsetY),
                        end = Offset(x, offsetY + gridHeight),
                        strokeWidth = strokeWidthPx
                    )
                }
                for (i in 0..gridRow) {
                    val y = offsetY + i * tileSize
                    drawLine(
                        color = colorOutline,
                        start = Offset(offsetX, y),
                        end = Offset(offsetX + gridWidth, y),
                        strokeWidth = strokeWidthPx
                    )
                }
            }
        }
    }
}

fun cellCoordinatesAtOffset(
    hitPoint: Offset,
    tileSize: Float,
    gridRow: Int,
    gridColumn: Int,
    offsetX: Float,
    offsetY: Float
): Pair<Int, Int>? {
    if (tileSize <= 0) return null
    
    val relativeX = hitPoint.x - offsetX
    val relativeY = hitPoint.y - offsetY
    
    val col = (relativeX / tileSize).toInt()
    val row = (relativeY / tileSize).toInt()

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
