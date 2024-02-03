
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
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import model.game.EspaceCellulaire
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

@Composable
fun App() {

    MaterialTheme {
        val mutableState =
            MutableStateFlow(State(listOf()))
        val espace = EspaceCellulaire(20, 20)
        val gameViewModel = remember { GameViewModel() }

        val onCellClick: (Pair<Int, Int>) -> Unit = { cellCoord ->
            espace[cellCoord]?.estVivante = !espace[cellCoord]?.estVivante!!
            mutableState.value = State(espace.getVivantes().map { Pair(it.first, it.second) })

        }



        espace.setVivantes(*mutableState.value.colored.toTypedArray())
        game(
            rememberCoroutineScope(),
            mutableState,
            espace,
            gameViewModel
        )
        val state: Flow<State> = mutableState
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {
            GameOfLife(state, gameViewModel, onCellClick, mutableState)
        }
    }
}

data class State(val colored: List<Pair<Int, Int>>)


@OptIn(ExperimentalResourceApi::class)
@Composable
fun Bouttons(gameViewModel: GameViewModel){
    //play button with play icon

    Button(
        onClick = { gameViewModel.togglePause() },
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        val painter = painterResource("baseline_pause_24.xml")
        if (gameViewModel.isPaused.value) {
            Icon(painter, contentDescription = "pause")
        } else {
            Icon(
                imageVector = Icons.Filled.PlayArrow,
                contentDescription = "Play"
            )
        }



    }
}



fun game(
    scope: CoroutineScope,
    mutableState: MutableStateFlow<State>,
    cellularSpace: EspaceCellulaire,
    gameViewModel: GameViewModel
) {
    val mutex = Mutex()
    scope.launch {

        while (true) {
            delay(150)

            if (gameViewModel.isPaused.value) {
                mutex.withLock {
                    cellularSpace.evoluer()
                    mutableState.update {
                        State(cellularSpace.getVivantes().map { Pair(it.first, it.second) })
                    }
                }
            }

        }

    }
}


@Composable
fun GameOfLife(
    state: Flow<State>,
    gameViewModel: GameViewModel,
    onCellClick: (Pair<Int, Int>) -> Unit,
    mutableState: MutableStateFlow<State>
) {
    val stateElement = state.collectAsState(initial = null)

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        stateElement.value?.let {
            Board(mutableState, onCellClick)
        }
        Bouttons(gameViewModel)
    }

}

internal data class MiniState(val colored: List<Pair<Int, Int>>)
internal val LocalDragTargetInfo = compositionLocalOf { DragTargetInfo() }

internal class DragTargetInfo {
    var isDragging: Boolean by mutableStateOf(false)
    var dragPosition by mutableStateOf(Offset.Zero)
    var dragOffset by mutableStateOf(Offset.Zero)
    var draggableComposable by mutableStateOf<(@Composable () -> Unit)?>(null)
    var dataToDrop by mutableStateOf<Any?>(null)
}


@Composable
fun Board(mutableState: MutableStateFlow<State>,onCellClick: (Pair<Int, Int>) -> Unit) {

    val scroll = rememberLazyGridState()

    var drag = false

    val miniStateBundle = MiniState(listOf(Pair(0, 0), Pair(0, 1), Pair(1, 1), Pair(1,0)))

    //when drag via https://blog.canopas.com/android-drag-and-drop-ui-element-in-jetpack-compose-14922073b3f1
    //mettre le bundle dans l'autre grille

    //small lazy 5*5 grid
    LazyVerticalGrid(GridCells.Fixed(5)) {
        items(25) {
            val cellCoord = Pair(it / 5, it % 5)
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .aspectRatio(1f) // Assure que la boîte est un carré
                    .background(
                        if (miniStateBundle.colored.contains(cellCoord)) Color.Black else Color.White
                    )
                    .border(1.dp, Color.Gray)
            )
        }
    }


    LazyVerticalGrid(GridCells.Fixed(20),state = scroll, modifier = Modifier.pointerInput(Unit) {
        fun cellCoordAtOffset(hitPoint: Offset): Pair<Int, Int> {
            //tilesize - scrollstate


            val tileSize = size.width / 20
            val x = (hitPoint.x / tileSize).toInt()
            val y = (hitPoint.y / tileSize).toInt() + scroll.firstVisibleItemIndex / 20
            return Pair(y, x)
        }
        var currentCellCoord = Pair(0, 0)
        var initCellCoord = Pair(0, 0)

        detectDragGestures (
            onDragStart = { offset ->
                cellCoordAtOffset(offset).let {pair ->
                    if (!mutableState.value.colored.contains(pair)) {
                        currentCellCoord = pair
                        onCellClick(pair)
                        drag = false
                    }else{
                        initCellCoord = pair
                        drag = true
                    }
                }
            },


            onDrag = { change, _ ->
                cellCoordAtOffset(change.position).let { pointerCellCoord ->
                    if (currentCellCoord != pointerCellCoord) {
                        if (!drag) {
                            onCellClick(currentCellCoord)
                        }
                        currentCellCoord = pointerCellCoord
                    }
                }
            },
            onDragEnd = {
                onCellClick(initCellCoord)


                onCellClick(currentCellCoord)

            }
        )

        /*detectDragGestures { change, dragAmount ->
            change.consume()
            cellCoordAtOffset(change.previousPosition).let {
                onCellClick(it)
            }

            cellCoordAtOffset(change.position).let {
                addCell(it)
            }
        }*/
    }) {
        items(400) {
            val cellCoord = Pair(it / 20, it % 20)
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .aspectRatio(1f) // Assure que la boîte est un carré
                    .background(
                        if (mutableState.value.colored.contains(cellCoord)) Color.Black else Color.White
                    )
                    .border(1.dp, Color.Gray)
                    .clickable { onCellClick(cellCoord) }
            )
        }
    }

}