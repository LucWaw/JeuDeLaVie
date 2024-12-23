package kmp.project.gameoflife.ui.game

import kmp.project.gameoflife.spacing.CellularSpace
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock


fun runGameLoop(
    playScope: CoroutineScope,
    updateCells: (List<Pair<Int, Int>>) -> Unit,
    addToCounter: () -> Unit,
    cellularSpace: CellularSpace,
    buttonsViewModel: ButtonsViewModel,
    speedFlow: StateFlow<Float>
) {
    val mutex = Mutex()
    playScope.launch {
        speedFlow.collectLatest { speed ->
            val adjustedSpeed = maxOf(speed, 0.1f) // Éviter des vitesses trop lentes
            val delayTime = (150 / adjustedSpeed).toLong()

            var lastUpdateTime = System.currentTimeMillis()

            while (buttonsViewModel.isRunning) {
                val currentTime = System.currentTimeMillis()
                val deltaTime = currentTime - lastUpdateTime

                if (deltaTime >= delayTime) {
                    lastUpdateTime = currentTime

                    mutex.withLock {
                        cellularSpace.evolve() // Une seule évolution par tick
                        updateCells(cellularSpace.getAliveCells().map { Pair(it.first, it.second) })
                        addToCounter()
                    }
                }

                // Attendre une petite période pour éviter une boucle trop rapide
                delay(1L)
            }
        }
    }
}

