package kmp.project.gameoflife.ui

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Slider
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kmp.project.gameoflife.getPlatform
import kmp.project.gameoflife.ui.board.Board
import kmp.project.gameoflife.ui.draganddrop.LongPressDraggable
import kmp.project.gameoflife.ui.game.Buttons
import kmp.project.gameoflife.ui.pattern.PatternsUI


@Composable
fun GameOfLife(
    isTablet: Boolean = false,
    showOnboarding: () -> Unit,
    modifier: Modifier = Modifier
) {
    val gameOfLifeViewModel = remember { GameOfLifeViewModel() }
    val gameUIState by gameOfLifeViewModel.mutableGameUiState.collectAsState()
    val isDesktop: Boolean = getPlatform().name.startsWith("Java")

    val gridRow = getGridRow()
    val gridColumn = getGridColumn()

    if (isTablet) {
        LaunchedEffect(Unit) {
            gameOfLifeViewModel.initCellularSpace(
                20,
                80
            )
        }
    } else {
        LaunchedEffect(Unit) {
            gameOfLifeViewModel.initCellularSpace(
                gridRow,
                gridColumn
            )
        }
    }


    Column {
        LongPressDraggable(
            modifier = modifier.width(2000.dp),
            gameOfLifeViewModel.gridSize,
            if (isDesktop || isTablet) 80 else 15
        ) {//gameUIState.gridSize can't change
//gameUIState.gridSize can't change
            Column {

                //Game board
                gameOfLifeViewModel.mutableGameUiState.collectAsState().value.let { gameUiState -> //ou if stateElement.value != null
                    Board(
                        isTablet,
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
            gameOfLifeViewModel.cellularSpace.value,
            { gameOfLifeViewModel.updateCells(it) },
            { gameOfLifeViewModel.addToCounter() },
            gameOfLifeViewModel.speedState,
        )


        Row(Modifier.padding(horizontal = 16.dp), verticalAlignment = Alignment.CenterVertically) {
            SliderSpeed(onPositionChange = { gameOfLifeViewModel.changeSpeedGeneration(it) })
            Spacer(Modifier.weight(1f))
            IconButton(
                onClick = {
                    showOnboarding()
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = "Refresh"
                )
            }
            Spacer(Modifier.weight(1f))
            generationCounter(gameUIState)
        }
    }
}

@Composable
fun SliderSpeed(onPositionChange: (Float) -> Unit) {
    var sliderPosition by remember { mutableFloatStateOf(1f) }
    Column(Modifier.width(200.dp)) {
        Slider(
            value = sliderPosition,
            onValueChange = {
                sliderPosition = it
                onPositionChange(it)
            }
        )
    }
}


@Composable
private fun generationCounter(gameUIState: GameUiState) {
    Row(
        modifier = Modifier
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
