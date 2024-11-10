package ui.game

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import model.spacing.CellularSpace


fun runGameLoop(
    playScope: CoroutineScope,
    updateCells: (List<Pair<Int, Int>>) -> Unit,
    addToCounter: () -> Unit,
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
                updateCells(cellularSpace.getAliveCells().map { Pair(it.first, it.second) })
                addToCounter()
            }
        }
    }
}