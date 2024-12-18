package ui

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
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
        LongPressDraggable(
            modifier = modifier.width(2000.dp),
            gameOfLifeViewModel.gridSize,
            gameUIState.gridColumn * gameUIState.gridRow
        ) {//gameUIState.gridSize can't change
            Column {

                //Game board
                gameOfLifeViewModel.mutableGameUiState.collectAsState().value.let { gameUiState -> //ou if stateElement.value != null
                    Board(
                        gameUiState,
                        gameOfLifeViewModel.onCellClick,
                        gameOfLifeViewModel.gridSize,
                        { gameOfLifeViewModel.modifyGridSize(it) },
                        modifier
                    )
                }

                PatternsUI()
            }
        }
        //Play pause button
        val playScope = rememberCoroutineScope()
        Buttons(
            playScope,
            gameOfLifeViewModel.cellularSpace,
            { gameOfLifeViewModel.updateCells(it) },
            { gameOfLifeViewModel.addToCounter() },
            modifier
        )


        Row {
            Spacer(Modifier.weight(1f))

            generationCounter(gameUIState)
            Spacer(Modifier.size(16.dp))


        }
    }
}



@Composable
private fun generationCounter(gameUIState: GameUiState) {
    Row(
        modifier = Modifier
            .height(30.dp)
            .border(
                width = 2.dp,
                color = MaterialTheme.colors.primary,
                shape = RoundedCornerShape(
                    topStartPercent = 50,
                    bottomStartPercent = 50,
                    topEndPercent = 50,
                    bottomEndPercent = 50
                )
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(Modifier.width(12.dp))
        Text(
            text = "Generation #${gameUIState.generationCounter}",
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.width(12.dp))

    }
}
