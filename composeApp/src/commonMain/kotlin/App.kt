import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
            gameViewModel)
        val state: Flow<State> = mutableState
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {
            GameOfLife(state, gameViewModel, onCellClick)
        }
    }
}

data class State(val colored: List<Pair<Int, Int>>)


@Composable
fun Bouttons(gameViewModel: GameViewModel){
    //play button with play icon
    Button(
        onClick = { gameViewModel.togglePause() },
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Icon(
            imageVector = Icons.Filled.PlayArrow,
            contentDescription = "Play"
        )
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
fun GameOfLife(state: Flow<State>, gameViewModel: GameViewModel, onCellClick: (Pair<Int, Int>) -> Unit) {
    val stateElement = state.collectAsState(initial = null)

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        stateElement.value?.let {
            Board(it, onCellClick)
        }
        Bouttons(gameViewModel)
    }

}


@Composable
fun Board(state: State, onCellClick: (Pair<Int, Int>) -> Unit ) {


    //si le click est laisé appuyé mousemove




    BoxWithConstraints(Modifier.padding(16.dp)) {
        val tileSize = maxWidth / 20

        Box(
            Modifier
                .size(maxWidth)
                .border(2.dp, Color.Black)
        )


        for (i in 0 until 20) {
            for (j in 0 until 20) {
                val cellCoord = Pair(i, j)
                Box(
                    modifier = Modifier
                        .offset(x = tileSize * i, y = tileSize * j)
                        .size(tileSize)
                        .background(
                            if (state.colored.contains(cellCoord)) Color.Black else Color.White
                        )
                        .border(1.dp, Color.Gray)
                        .clickable { onCellClick(cellCoord) }

                )
            }
        }
    }
}