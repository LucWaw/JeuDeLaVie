
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import model.game.CellularSpace
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

const val GRID_SIZE = 20

@Composable
fun App() {

    MaterialTheme {
        val mutableState =
            MutableStateFlow(State(listOf()))
        val space = CellularSpace(GRID_SIZE, GRID_SIZE)
        val gameViewModel = remember { GameViewModel() }
        val onCellClick: (Pair<Int, Int>) -> Unit = { cellCoordinates ->
            space[cellCoordinates]?.isAlive = !space[cellCoordinates]?.isAlive!!
            mutableState.value = State(space.getAliveCells().map { Pair(it.first, it.second) })
        }

        val playScope = rememberCoroutineScope()

        space.setAliveCells(*mutableState.value.colored.toTypedArray())

        val state: Flow<State> = mutableState
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {
            GameOfLife(state, playScope, gameViewModel, onCellClick, mutableState, space)
        }
    }
}

data class State(val colored: List<Pair<Int, Int>>)



fun toggleGameLoop(
    mutableState: MutableStateFlow<State>,
    playScope: CoroutineScope,
    space: CellularSpace,
    gameViewModel: GameViewModel
) {
    gameViewModel.togglePause() // Met le jeu en pause ou en marche

    if (gameViewModel.isRunning.value) {
        runGameLoop(
            playScope,
            mutableState,
            space,
            gameViewModel
        )
    }
}



fun runGameLoop(
    playScope: CoroutineScope,
    mutableState: MutableStateFlow<State>,
    cellularSpace: CellularSpace,
    gameViewModel: GameViewModel
) {
    val mutex = Mutex()
    playScope.launch {
        while (gameViewModel.isRunning.value) {

            delay(150)

                //mutex pour éviter l' accès concurrent à cellularSpace
                mutex.withLock {
                    cellularSpace.evolve()
                    mutableState.update {
                        State(cellularSpace.getAliveCells().map { Pair(it.first, it.second) })
                    }
                }

        }
    }
}


@Composable
fun GameOfLife(
    state: Flow<State>,
    playScope: CoroutineScope,
    gameViewModel: GameViewModel,
    onCellClick: (Pair<Int, Int>) -> Unit,
    mutableState: MutableStateFlow<State>,
    space: CellularSpace
) {
    val stateElement = state.collectAsState(initial = null)

    Column(horizontalAlignment = Alignment.CenterHorizontally) {

        stateElement.value?.let {
            Board(it, onCellClick)
        }

        Buttons(gameViewModel, playScope, space, mutableState)
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun Buttons(
    gameViewModel: GameViewModel,
    playScope: CoroutineScope,
    cellularSpace: CellularSpace,
    mutableState: MutableStateFlow<State>
) {
    //play button with play icon
    Button(
        onClick = { toggleGameLoop(mutableState, playScope, cellularSpace, gameViewModel)},
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        val painter = painterResource("baseline_pause_24.xml")
        if (gameViewModel.isRunning.value) {
            Icon(painter, contentDescription = "pause")
        } else {
            Icon(
                imageVector = Icons.Filled.PlayArrow,
                contentDescription = "Play"
            )
        }
    }
}

@Composable
fun Board(state: State, onCellClick: (Pair<Int, Int>) -> Unit) {
    val scroll = rememberLazyGridState()
    var gridSize by remember { mutableStateOf(Size.Zero) } // To store the actual size of the grid

    LazyVerticalGrid(
        GridCells.Fixed(GRID_SIZE),
        state = scroll,
        modifier = Modifier
            .pointerInput(Unit) {
                fun cellCoordinatesAtOffset(hitPoint: Offset): Pair<Int, Int> {
                    // Calculate the actual size of each cell
                    val tileSize = gridSize.width / GRID_SIZE
                    val x = (hitPoint.x / tileSize).toInt()
                    val y = (hitPoint.y / tileSize).toInt()
                    return Pair(y, x)
                }

                var currentCellCoordinates = Pair(0, 0)

                detectDragGestures(
                    onDragStart = { offset ->
                        cellCoordinatesAtOffset(offset).let {
                            if (!state.colored.contains(it)) {
                                currentCellCoordinates = it
                                onCellClick(it)
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
        items(400) { index ->
            val cellCoordinates = Pair(index / GRID_SIZE, index % GRID_SIZE)
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .aspectRatio(1f)
                    .background(if (state.colored.contains(cellCoordinates)) Color.Black else Color.White)
                    .border(1.dp, Color.Gray)
                    .clickable { onCellClick(cellCoordinates) }
            )
        }
    }
}