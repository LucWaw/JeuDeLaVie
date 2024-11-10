package ui

import androidx.compose.ui.geometry.Size
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import model.spacing.CellularSpace

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
        _mutableGameUiState.update { currentState ->
            currentState.copy(
                colored = colored
            )
        }
    }

    fun addToCounter(){
        if(mutableGameUiState.value.colored.isEmpty()){
            resetCounter()
            return
        }

        _mutableGameUiState.update { currentState ->
            currentState.copy(
                generationCounter = currentState.generationCounter + 1
            )
        }
    }

    private fun resetCounter(){
        _mutableGameUiState.update { currentState ->
            currentState.copy(
                generationCounter = 0
            )
        }
    }

    private val _gridSize : MutableStateFlow<Size> = MutableStateFlow(Size.Zero) // To store the actual size of the grid
    val gridSize : StateFlow<Size> = _gridSize.asStateFlow()

    fun modifyGridSize(gridSize: Size){
        _gridSize.value = gridSize
    }
}