package kmp.project.gameoflife.ui.game

import kmp.project.gameoflife.spacing.CellularSpace
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
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
        while (buttonsViewModel.isRunning) {
            // Lire la vitesse actuelle sans relancer toute la boucle
            val speed = speedFlow.first() // Prend la valeur actuelle sans suspendre la boucle
            val adjustedSpeed = maxOf(speed, 0.1f)
            val delayTime = (150 / adjustedSpeed).toLong()


            mutex.withLock {
                cellularSpace.evolve()
                updateCells(cellularSpace.getAliveCells().map { Pair(it.first, it.second) })
                addToCounter()
            }

            delay(delayTime) // Attente en fonction de la vitesse actuelle
        }
    }
}




