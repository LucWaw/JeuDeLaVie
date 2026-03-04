package kmp.project.gameoflife.ui

import androidx.compose.ui.geometry.Size
import androidx.lifecycle.ViewModel
import kmp.project.gameoflife.spacing.CellularSpace
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class GameOfLifeViewModel : ViewModel() {

    private val _mutableGameUiState =
        MutableStateFlow(GameUiState())


    val mutableGameUiState: StateFlow<GameUiState> = _mutableGameUiState.asStateFlow()

    private val _cellularSpace = MutableStateFlow(CellularSpace(15, 15))
    val cellularSpace: StateFlow<CellularSpace> = _cellularSpace.asStateFlow()

    private var _previousGrid: List<Pair<Int, Int>> = emptyList()
    val previousGrid: List<Pair<Int, Int>> get() = _previousGrid

    private val _speedGeneration = MutableStateFlow(1f)
    val speedState : StateFlow<Float> = _speedGeneration.asStateFlow()


    fun initCellularSpace(gridRow : Int, gridColumn : Int) {
        _cellularSpace.update {
            CellularSpace(gridRow, gridColumn)
        }
    }

    val onCellClick: (Pair<Int, Int>) -> Unit = { cellCoordinates ->
        cellularSpace.value[cellCoordinates]?.isAlive = !cellularSpace.value[cellCoordinates]?.isAlive!!
        _mutableGameUiState.update { currentState ->
            currentState.copy(
                colored = cellularSpace.value.getAliveCells()
            )
        }
    }


    fun updateCells(colored: List<Pair<Int, Int>>) {
        _mutableGameUiState.update { currentState ->
            currentState.copy(
                colored = colored
            )
        }
    }

    fun resetGrid() {
        cellularSpace.value.resetGrid()
        _mutableGameUiState.update { currentState ->
            currentState.copy(
                colored = emptyList(),
                generationCounter = 0
            )
        }
    }

    fun changeSpeedGeneration(speed: Float){
        _speedGeneration.update {
            speed
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

    private val _gridSize : MutableStateFlow<Size> = MutableStateFlow(Size.Zero)
    val gridSize : StateFlow<Size> = _gridSize.asStateFlow()

    fun modifyGridSize(gridSize: Size){
        _gridSize.value = gridSize
    }

    fun capturePreviousGrid() {
        _previousGrid = _mutableGameUiState.value.colored
    }
}
