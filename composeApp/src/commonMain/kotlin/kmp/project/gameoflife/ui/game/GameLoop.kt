package kmp.project.gameoflife.ui.game

import kmp.project.gameoflife.spacing.CellularSpace
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.StateFlow

suspend fun runGameLoop(
    updateCells: (List<Pair<Int, Int>>) -> Unit,
    addToCounter: () -> Unit,
    cellularSpace: CellularSpace,
    speedFlow: StateFlow<Float>
) {
    while (true) {
        // Lire la vitesse actuelle
        val speed = speedFlow.value
        val adjustedSpeed = maxOf(speed, 0.1f)
        val delayTime = (150 / adjustedSpeed).toLong()

        cellularSpace.evolve()
        updateCells(cellularSpace.getAliveCells().map { Pair(it.first, it.second) })
        addToCounter()

        delay(delayTime) // Attente en fonction de la vitesse actuelle
    }
}
