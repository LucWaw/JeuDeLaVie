package ui

import data.State
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import model.Space.CellularSpace
import ui.ViewModels.GameViewModel


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