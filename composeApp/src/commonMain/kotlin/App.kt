
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import kotlinx.coroutines.flow.MutableStateFlow
import model.Space.CellularSpace
import ui.GameOfLife
import ui.GameUIState
import ui.draganddrop.DragAndDropTheme

const val GRID_SIZE = 15

@Composable
fun App() {

    DragAndDropTheme {
        val mutableGameUIState =
            MutableStateFlow(GameUIState(emptyList()))

        val cellularSpace = CellularSpace(GRID_SIZE, GRID_SIZE)
        val onCellClick: (Pair<Int, Int>) -> Unit = { cellCoordinates ->
            cellularSpace[cellCoordinates]?.isAlive = !cellularSpace[cellCoordinates]?.isAlive!!
            mutableGameUIState.value =
                GameUIState(cellularSpace.getAliveCells().map { Pair(it.first, it.second) })
        }



        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {
            GameOfLife(onCellClick, mutableGameUIState, cellularSpace)
        }
    }
}


