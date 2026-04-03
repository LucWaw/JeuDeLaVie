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
        
        // Calculate the largest possible tile size that allows the entire grid to fit.
        // We use min() to ensure that the grid is constrained by the smaller dimension (width or height).
        val tileSize = min(availableWidth / gridColumn, availableHeight / gridRow)
        
        // Compute total grid dimensions based on the calculated tile size.
        val gridWidth = tileSize * gridColumn
        val gridHeight = tileSize * gridRow
        
        // Calculate offsets to center the grid within the available BoxWithConstraints area.
        // Subtracting grid size from available size and dividing by 2 gives the starting padding.
        val offsetX = (availableWidth - gridWidth) / 2
        val offsetY = (availableHeight - gridHeight) / 2

        CustomDropTarget(
            modifier = Modifier
                .fillMaxSize()
                .onSizeChanged { gridChange(it.toSize()) },
            onDropPattern = { pattern, dropOffset ->
                // Translate the drop screen offset to a position relative to the grid's top-left corner.
                val relativeX = dropOffset.x - offsetX
                val relativeY = dropOffset.y - offsetY

                // Calculate the theoretical grid position based on the drop offset.
                val rawDropRow = (relativeY / tileSize).toInt()
                val rawDropCol = (relativeX / tileSize).toInt()

                // Calculate the pattern offset to handle centering or relative placement.
                val patternOffset = pattern.gridSize - 1

                // ADJUSTMENT: Constrain the drop coordinates to ensure the pattern remains within grid boundaries.
                // Ensures top edge (dropRow - patternOffset) >= 0 and bottom edge <= gridRow.
                val dropRow = rawDropRow.coerceIn(patternOffset, gridRow - 1)
                val dropCol = rawDropCol.coerceIn(patternOffset, gridColumn - 1)

                // Iterate through pattern cells and toggle the corresponding grid cells if they are within bounds.
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
                    // pointerInput allows us to handle low-level touch/mouse events.
                    // We pass keys (tileSize, etc.) to restart the pointer processing if layout changes.
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
                                    // When dragging fast, some cells might be skipped. 
                                    // interpolateCells uses a line algorithm to find all cells between the last and current position.
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

                    //Useful for desktop target
                    .pointerInput(tileSize, gridRow, gridColumn, offsetX, offsetY) {
                        // awaitPointerEventScope provides a suspendable scope to listen for raw pointer events.
                        // This is useful for events like Move or Enter/Exit that aren't covered by standard gesture detectors.
                        awaitPointerEventScope {
                            while (true) {
                                // Suspend until a pointer event (mouse move, touch, etc.) occurs.
                                val event = awaitPointerEvent()
                                val position = event.changes.first().position
                                
                                when (event.type) {
                                    // Update hover state when the pointer moves or enters the canvas area.
                                    PointerEventType.Move, PointerEventType.Enter -> {
                                        hoverCell = cellCoordinatesAtOffset(position, tileSize, gridRow, gridColumn, offsetX, offsetY)
                                    }
                                    // Clear hover when the pointer leaves or is released.
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

                // Draw alive cells: iterate over the set of active cells and render them as rectangles.
                gameUIState.colored.forEach { (row, col) ->
                    if (row in 0 until gridRow && col in 0 until gridColumn) {
                        // drawRect renders a filled rectangle. 
                        // Math: topLeft = (startOffset + index * cellSize) converts grid index to pixel space.
                        drawRect(
                            color = colorPrimary,
                            topLeft = Offset(offsetX + col * tileSize, offsetY + row * tileSize),
                            size = Size(tileSize, tileSize),
                            style = Fill
                        )
                    }
                }

                // Draw hover highlight: provides visual feedback for where the next click/drag will hit.
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

                // Draw grid lines: use drawLine to create the wireframe of the board.
                // Vertical lines: iterate columns from 0 to N.
                for (i in 0..gridColumn) {
                    val x = offsetX + i * tileSize
                    // drawLine draws a straight line between two Offset points.
                    drawLine(
                        color = colorOutline,
                        start = Offset(x, offsetY),
                        end = Offset(x, offsetY + gridHeight),
                        strokeWidth = strokeWidthPx
                    )
                }
                // Horizontal lines: iterate rows from 0 to M.
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

/**
 * Translates a raw pixel [Offset] (from touch or mouse) into grid coordinates.
 * 
 * Goal: Determine which cell was hit.
 * Math: (hitPoint - offset) / tileSize. We subtract the centering offset, then divide
 * by the cell size to get the fractional index, which is then floored to Int.
 */
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

    // Bounds check to ensure the calculated index is within the actual grid.
    if (col in 0 until gridColumn && row in 0 until gridRow) {
        return Pair(row, col)
    }
    return null
}

/**
 * Implements Bresenham's line algorithm for grid cells.
 * 
 * Goal: Find all grid cells that lie on a straight line between two points.
 * Why it works: It uses integer arithmetic to determine the "error" from a perfect line.
 * It steps along the primary axis and decides whether to increment the secondary axis based on 
 * an accumulated error term, ensuring we hit every cell the line passes through without floating point drift.
 */
fun interpolateCells(start: Pair<Int, Int>, end: Pair<Int, Int>): List<Pair<Int, Int>> {
    val result = mutableListOf<Pair<Int, Int>>()
    var x0 = start.second
    var y0 = start.first
    val x1 = end.second
    val y1 = end.first
    
    val dx = abs(x1 - x0)
    val dy = abs(y1 - y0)
    val sx = if (x0 < x1) 1 else -1 // Direction step for X
    val sy = if (y0 < y1) 1 else -1 // Direction step for Y
    var err = dx - dy // Initial error value
    
    while (true) {
        result.add(Pair(y0, x0))
        if (x0 == x1 && y0 == y1) break
        
        val e2 = 2 * err
        // If error in X is within threshold, step in X and adjust error.
        if (e2 > -dy) {
            err -= dy
            x0 += sx
        }
        // If error in Y is within threshold, step in Y and adjust error.
        if (e2 < dx) {
            err += dx
            y0 += sy
        }
    }
    return result
}
