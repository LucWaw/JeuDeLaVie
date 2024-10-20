package ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ui.board.Board
import ui.draganddrop.LongPressDraggable
import ui.game.Buttons
import ui.pattern.PatternsUI


@Composable
fun GameOfLife(
    modifier: Modifier = Modifier
) {
    val gameOfLifeViewModel = remember { GameOfLifeViewModel() }
    val gameUIState by gameOfLifeViewModel.mutableGameUiState.collectAsState()

    Column {
        LongPressDraggable(modifier = modifier.width(1000.dp), gameOfLifeViewModel.gridSize, gameUIState.gridSize) {//gameUIState.gridSize can't change
            Column {

                //Game board
                gameOfLifeViewModel.mutableGameUiState.collectAsState().value.let { gameUiState -> //ou if stateElement.value != null
                    Board(gameUiState, gameOfLifeViewModel.onCellClick, gameOfLifeViewModel.gridSize, { gameOfLifeViewModel.modifyGridSize(it) }, modifier)
                }

                PatternsUI()
            }
        }
        //Play pause button
        val playScope = rememberCoroutineScope()
        Buttons(playScope, gameOfLifeViewModel.cellularSpace,
            {  gameOfLifeViewModel.updateCells(it) }, modifier)
    }
}