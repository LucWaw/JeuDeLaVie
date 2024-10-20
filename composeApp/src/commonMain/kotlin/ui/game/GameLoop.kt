package ui.game

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import model.space.CellularSpace


fun runGameLoop(
    playScope: CoroutineScope,
    mutableGameUIState: (List<Pair<Int, Int>>) -> Unit,
    cellularSpace: CellularSpace,
    buttonsViewModel: ButtonsViewModel
) {
    val mutex = Mutex()
    playScope.launch {
        while (buttonsViewModel.isRunning) {

            delay(150)

            //mutex pour éviter l' accès concurrent à cellularSpace
            mutex.withLock {
                cellularSpace.evolve()
                mutableGameUIState(cellularSpace.getAliveCells().map { Pair(it.first, it.second) })
            }
        }
    }
}