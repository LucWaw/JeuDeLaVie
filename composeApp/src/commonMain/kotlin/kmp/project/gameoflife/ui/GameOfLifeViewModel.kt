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

    private var _previousGrid: Set<Pair<Int, Int>> = emptySet()
    val previousGrid: Set<Pair<Int, Int>> get() = _previousGrid

    private val _speedGeneration = MutableStateFlow(1f)
    val speedState : StateFlow<Float> = _speedGeneration.asStateFlow()


    fun initCellularSpace(gridRow : Int, gridColumn : Int) {
        _cellularSpace.update {
            CellularSpace(gridRow, gridColumn)
        }
        _mutableGameUiState.update { currentState ->
            currentState.copy(
                colored = cellularSpace.value.getAliveCells()
            )
        }
    }

    val onCellClick: (Pair<Int, Int>) -> Unit = { cellCoordinates ->
        val cell = cellularSpace.value[cellCoordinates]
        if (cell != null) {
            cell.isAlive = !cell.isAlive
            _mutableGameUiState.update { currentState ->
                val newColored = currentState.colored.toMutableSet()
                if (cell.isAlive) {
                    newColored.add(cellCoordinates)
                } else {
                    newColored.remove(cellCoordinates)
                }
                currentState.copy(colored = newColored)
            }
        }
    }

    fun toggleCell(cellCoordinates: Pair<Int, Int>, forceAlive: Boolean? = null) {
        val cell = cellularSpace.value[cellCoordinates]
        if (cell != null) {
            val shouldBeAlive = forceAlive ?: !cell.isAlive
            if (cell.isAlive != shouldBeAlive) {
                cell.isAlive = shouldBeAlive
                _mutableGameUiState.update { currentState ->
                    val newColored = currentState.colored.toMutableSet()
                    if (shouldBeAlive) {
                        newColored.add(cellCoordinates)
                    } else {
                        newColored.remove(cellCoordinates)
                    }
                    currentState.copy(colored = newColored)
                }
            }
        }
    }


    fun updateCells(colored: Set<Pair<Int, Int>>) {
        // First sync cellular space
        cellularSpace.value.resetGrid()
        colored.forEach { cellularSpace.value[it]?.isAlive = true }
        
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
                colored = emptySet(),
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
