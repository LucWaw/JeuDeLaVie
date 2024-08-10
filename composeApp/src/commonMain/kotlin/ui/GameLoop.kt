package ui

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import data.MiniState
import data.State
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import model.Space.CellularSpace
import ui.ViewModels.GameViewModel
import ui.pattern.patternSquare


fun runGameLoop(
    playScope: CoroutineScope,
    mutableState: MutableStateFlow<State>,
    cellularSpace: CellularSpace,
    gameViewModel: GameViewModel
) {
    val mutex = Mutex()
    playScope.launch {
        while (gameViewModel.isRunning) {

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
    onCellClick: (Pair<Int, Int>) -> Unit,
    mutableState: MutableStateFlow<State>,
    space: CellularSpace,
    modifier: Modifier = Modifier
) {
    val stateElement = state.collectAsState(initial = null)

    Column(modifier = modifier) {
        patternSquare()


        stateElement.value?.let {
            DropTarget<MiniState> (
                modifier = Modifier
            ) { isInBound, patternlistOfCoordinates ->
                val alives = patternlistOfCoordinates?.colored ?: emptyList()

                Board(it, alives, onCellClick)

            }

        }

        Buttons(playScope, space, mutableState)
    }
}