package kmp.project.gameoflife.ui

import kmp.project.gameoflife.spacing.CellularSpace
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class GameOfLifeViewModel {

    private val _mutableGameUiState =
        MutableStateFlow(GameUiState())


    val mutableGameUiState: StateFlow<GameUiState> = _mutableGameUiState.asStateFlow()



    private val _cellularSpace = MutableStateFlow(CellularSpace(15, 15))
    val cellularSpace: StateFlow<CellularSpace> = _cellularSpace.asStateFlow()

    private val _speedGeneration = MutableStateFlow(1f)
    val speedState : StateFlow<Float> = _speedGeneration.asStateFlow()


    fun initCellularSpace(gridRow : Int, gridColumn : Int) {
        _cellularSpace.update {
            CellularSpace(gridRow, gridColumn)
        }
    }

    val onCellClick: (Pair<Int, Int>) -> Unit = { cellCoordinates ->
        cellularSpace.value[cellCoordinates]?.isAlive = !cellularSpace.value[cellCoordinates]?.isAlive!!
        _mutableGameUiState.value =
            GameUiState(cellularSpace.value.getAliveCells().map { Pair(it.first, it.second) })
    }


    fun updateCells(colored: List<Pair<Int, Int>>) {
        _mutableGameUiState.update { currentState ->
            currentState.copy(
                colored = colored
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


}