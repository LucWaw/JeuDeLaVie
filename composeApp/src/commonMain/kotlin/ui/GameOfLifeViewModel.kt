package ui

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import model.Space.CellularSpace

class GameOfLifeViewModel {

    private val _mutableGameUiState =
        MutableStateFlow(GameUiState(emptyList()))

    val mutableGameUiState: StateFlow<GameUiState> = _mutableGameUiState.asStateFlow()


    val cellularSpace =
        CellularSpace(mutableGameUiState.value.gridSize, mutableGameUiState.value.gridSize)

    val onCellClick: (Pair<Int, Int>) -> Unit = { cellCoordinates ->
        cellularSpace[cellCoordinates]?.isAlive = !cellularSpace[cellCoordinates]?.isAlive!!
        _mutableGameUiState.value =
            GameUiState(cellularSpace.getAliveCells().map { Pair(it.first, it.second) })
    }

    fun updateCells(colored: List<Pair<Int, Int>>) {
        _mutableGameUiState.update { currenState ->
            currenState.copy(
                colored = colored
            )
        }
    }
}