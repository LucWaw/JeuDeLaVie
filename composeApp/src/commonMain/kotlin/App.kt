
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import data.State
import game.GameOfLife
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import model.game.CellularSpace

const val GRID_SIZE = 20

@Composable
fun App() {

    MaterialTheme {
        val gliderPattern = listOf(
        Pair(1, 0),
        Pair(2, 1),
        Pair(0, 2),
        Pair(1, 2),
        Pair(2, 2)
        )
        val mutableState =
            MutableStateFlow(State(gliderPattern))

        val space = CellularSpace(GRID_SIZE, GRID_SIZE)
        //val gameViewModel = remember { GameViewModel() }
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
            GameOfLife(state, playScope, onCellClick, mutableState, space)
        }
    }
}


