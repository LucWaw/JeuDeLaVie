package ui

import GRID_SIZE
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import data.State
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import model.Space.CellularSpace
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import ui.ViewModels.GameViewModel

@OptIn(ExperimentalResourceApi::class)
@Composable
fun Buttons(
    playScope: CoroutineScope,
    cellularSpace: CellularSpace,
    mutableState: MutableStateFlow<State>,
    modifier: Modifier = Modifier
) {
    //play button with play icon
    val gameViewModel = remember { GameViewModel() }
    Button(
        onClick = {
            gameViewModel.togglePause() // Met le jeu en pause ou en marche

            if (gameViewModel.isRunning) {
                runGameLoop( 
                    playScope,
                    mutableState,
                    cellularSpace,
                    gameViewModel
                )
            }
        },
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        val painter = painterResource("baseline_pause_24.xml")
        if (gameViewModel.isRunning) {
            Icon(painter, contentDescription = "pause")
        } else {
            Icon(
                imageVector = Icons.Filled.PlayArrow,
                contentDescription = "Play"
            )
        }
    }
}
var cell: Pair<Int, Int>? = null
var bundledCells: List<Pair<Int, Int>>? = null
var activated = false

@Composable
fun Board(state: State, onCellClick: (Pair<Int, Int>) -> Unit, modifier: Modifier = Modifier) {
    val scroll = rememberLazyGridState()
    var gridSize by remember { mutableStateOf(Size.Zero) } // To store the actual size of the grid


    fun cellCoordinatesAtOffset(hitPoint: Offset): Pair<Int, Int> {
        // Calculate the actual size of each cell
        val tileSize = gridSize.width / GRID_SIZE
        val x = (hitPoint.x / tileSize).toInt()
        val y = (hitPoint.y / tileSize).toInt()
        return Pair(y, x)
    }

    var currentPosition by mutableStateOf(Offset.Zero)


    LazyVerticalGrid(
        GridCells.Fixed(GRID_SIZE),
        state = scroll,
        modifier = Modifier
            .onGloballyPositioned {
                currentPosition = it.localToWindow(Offset.Zero)
            }
            .pointerInput(Unit) {
                var currentCellCoordinates = Pair(0, 0)

                detectDragGestures(
                    onDragStart = { offset ->
                        cellCoordinatesAtOffset(offset).let { pair ->
                            if (!state.colored.contains(pair)) {
                                currentCellCoordinates = pair
                                onCellClick(pair)
                            }
                        }
                    },
                    onDrag = { change, _ ->
                        cellCoordinatesAtOffset(change.position).let { pointerCellCoordinates ->
                            if (currentCellCoordinates != pointerCellCoordinates) {
                                onCellClick(pointerCellCoordinates)
                                currentCellCoordinates = pointerCellCoordinates
                            }
                        }
                    }
                )
            }
            .pointerInput(Unit) {
                var currentCellCoordinates = Pair(0, 0)

                detectDragGesturesAfterLongPress(
                    onDragStart = { offset ->
                        cellCoordinatesAtOffset(offset).let { pair ->
                            if (!state.colored.contains(pair)) {
                                currentCellCoordinates = pair
                                onCellClick(pair)
                            }
                        }
                    },
                    onDrag = { change, _ ->
                        cellCoordinatesAtOffset(change.position).let { pointerCellCoordinates ->
                            if (currentCellCoordinates != pointerCellCoordinates) {
                                onCellClick(pointerCellCoordinates)
                                currentCellCoordinates = pointerCellCoordinates
                            }
                        }
                    }
                )
            }
            .onSizeChanged { newSize ->
                gridSize = newSize.toSize() // Update the gridSize with the actual size
            }
    ) {
        items(GRID_SIZE * GRID_SIZE) { index ->
            val cellCoordinates = Pair(index / GRID_SIZE, index % GRID_SIZE)
            val interactionSource = remember { MutableInteractionSource() }

            DropTarget(modifier = modifier) { isInBound, bundleOfCells ->
                println("$isInBound and $bundleOfCells")
                if (isInBound && bundleOfCells != null) {
                    cell = cellCoordinates
                    bundledCells = bundleOfCells.cells
                    activated = true
                    println("LAa")
                }
                if (activated) {
                    activated = false

                    println("Here")

                    println("Hara")
                    println(cell)

                    bundledCells?.forEach { patternCell ->
                        if (cell != null) {
                            onCellClick(
                                Pair(
                                    patternCell.first + (cell?.first?:0),
                                    patternCell.second + (cell?.second ?: 0)
                                )
                            )
                        }

                    }
                }

                Box(
                    modifier = Modifier
                        .aspectRatio(1f)
                        .background(if (state.colored.contains(cellCoordinates) || interactionSource.collectIsHoveredAsState().value) Color.Black else Color.White)
                        .border(1.dp, Color.Gray)
                        .clickable { onCellClick(cellCoordinates) }
                        .hoverable(interactionSource = interactionSource)
                )
            }
        }
    }
}
