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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
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
import androidx.compose.ui.input.pointer.PointerInputChange
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

    Row(modifier = modifier.fillMaxWidth().padding(16.dp)){
        Button(
            onClick = {
                //effacer la grille
                cellularSpace.resetGrid()
                mutableState.value = State(mutableListOf())
            },
            modifier = modifier.weight(0.5f)){
            Icon(
                imageVector = Icons.Filled.Delete,
                contentDescription = "Play"
            )
        }
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
            modifier = modifier.weight(0.5f)
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
}
var cell: Pair<Int, Int>? = null
var bundledCells: List<Pair<Int, Int>>? = null
var activated = false

@Composable
fun Board(state: State, onCellClick: (Pair<Int, Int>) -> Unit, modifier: Modifier = Modifier) {
    val scroll = rememberLazyGridState()
    var gridSize by remember { mutableStateOf(Size.Zero) } // To store the actual size of the grid




    var currentPosition by mutableStateOf(Offset.Zero)

    var currentCellCoordinates = Pair(0, 0)
    val changeCurentCellCoordinates = { coordinate : Pair<Int, Int> ->
        currentCellCoordinates = coordinate
    }
    LazyVerticalGrid(
        GridCells.Fixed(GRID_SIZE),
        state = scroll,
        modifier = Modifier
            .onGloballyPositioned {
                currentPosition = it.localToWindow(Offset.Zero)
            }
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = { offset ->
                        dragStart(offset, state, gridSize, changeCurentCellCoordinates, onCellClick)
                    },
                    onDrag = { change, _ ->
                        drag(change, gridSize, currentCellCoordinates, changeCurentCellCoordinates, onCellClick)
                    }
                )
            }
            .pointerInput(Unit) {
                detectDragGesturesAfterLongPress(
                    onDragStart = { offset ->
                        dragStart(offset, state, gridSize, changeCurentCellCoordinates, onCellClick)
                    },
                    onDrag = { change, _ ->
                        drag(change, gridSize, currentCellCoordinates, changeCurentCellCoordinates, onCellClick)
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
                }
                if (activated) {
                    activated = false

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

private fun drag(
    change: PointerInputChange,
    gridSize: Size,
    currentCellCoordinates: Pair<Int, Int>,
    changeCurentCellCoordinates: (Pair<Int, Int>) -> Unit,
    onCellClick: (Pair<Int, Int>) -> Unit
) {
    cellCoordinatesAtOffset(change.position, gridSize).let { pointerCellCoordinates ->
        if (currentCellCoordinates != pointerCellCoordinates) {
            onCellClick(pointerCellCoordinates)
            changeCurentCellCoordinates(pointerCellCoordinates)
        }
    }
}

private fun dragStart(
    offset: Offset,
    state: State,
    gridSize: Size,
    changeCurentCellCoordinates: (Pair<Int, Int>) -> Unit,
    onCellClick: (Pair<Int, Int>) -> Unit
) {
    cellCoordinatesAtOffset(offset, gridSize).let { pair ->
        if (!state.colored.contains(pair)) {
            changeCurentCellCoordinates(pair)
            onCellClick(pair)
        }
    }
}
fun cellCoordinatesAtOffset(hitPoint: Offset, gridSize: Size): Pair<Int, Int> {
    // Calculate the actual size of each cell
    val tileSize = gridSize.width / GRID_SIZE
    val x = (hitPoint.x / tileSize).toInt()
    val y = (hitPoint.y / tileSize).toInt()
    return Pair(y, x)
}