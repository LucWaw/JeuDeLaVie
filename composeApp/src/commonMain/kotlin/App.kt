import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import ui.theme.DragAndDropTheme
import model.State
import kotlinx.coroutines.flow.MutableStateFlow
import model.Space.CellularSpace
import ui.GameOfLife

const val GRID_SIZE = 15

@Composable
fun App() {

    DragAndDropTheme {
        val mutableState =
            MutableStateFlow(State(emptyList()))

        val cellularSpace = CellularSpace(GRID_SIZE, GRID_SIZE)
        //val gameViewModel = remember { GameViewModel() }
        val onCellClick: (Pair<Int, Int>) -> Unit = { cellCoordinates ->
            cellularSpace[cellCoordinates]?.isAlive = !cellularSpace[cellCoordinates]?.isAlive!!
            mutableState.value =
                State(cellularSpace.getAliveCells().map { Pair(it.first, it.second) })
        }

        val playScope = rememberCoroutineScope()

        cellularSpace.setAliveCells(*mutableState.value.colored.toTypedArray())

        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {
            GameOfLife(playScope, onCellClick, mutableState, cellularSpace)
        }
    }
}


