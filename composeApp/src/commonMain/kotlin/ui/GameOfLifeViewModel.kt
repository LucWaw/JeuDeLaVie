package ui

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import model.Space.CellularSpace

class GameOfLifeViewModel {

    private val _mutableGameUIState =
        MutableStateFlow(GameUIState(emptyList()))

    val mutableGameUIState: StateFlow<GameUIState> = _mutableGameUIState.asStateFlow()


    val cellularSpace = CellularSpace(GRID_SIZE, GRID_SIZE)

    val onCellClick: (Pair<Int, Int>) -> Unit = { cellCoordinates ->
        cellularSpace[cellCoordinates]?.isAlive = !cellularSpace[cellCoordinates]?.isAlive!!
        _mutableGameUIState.value =
            GameUIState(cellularSpace.getAliveCells().map { Pair(it.first, it.second) })
    }

    fun updateCells(colored: List<Pair<Int, Int>>){
        _mutableGameUIState.update { currenState ->
            currenState.copy(
                colored = colored
            )
        }
    }
}