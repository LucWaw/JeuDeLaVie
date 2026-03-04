package kmp.project.gameoflife.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import gameoflife.composeapp.generated.resources.Res
import gameoflife.composeapp.generated.resources.info_24px
import kmp.project.gameoflife.ui.board.Board
import kmp.project.gameoflife.ui.game.Buttons
import kmp.project.gameoflife.ui.game.ButtonsViewModel
import kmp.project.gameoflife.ui.game.runGameLoop
import kmp.project.gameoflife.ui.pattern.MovablePatternViewModel
import kmp.project.gameoflife.ui.pattern.PatternsUI
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel


@Composable
fun GameOfLife(
    modifier: Modifier = Modifier,
    isTablet: Boolean = false,
    showOnboarding: () -> Unit,
) {
    val gameOfLifeViewModel = koinViewModel<GameOfLifeViewModel>()
    val gameUIState by gameOfLifeViewModel.mutableGameUiState.collectAsStateWithLifecycle()
    val buttonsViewModel = koinViewModel<ButtonsViewModel>()
    val patternViewModel = koinViewModel<MovablePatternViewModel>()

    val gridRow = getGridRow()
    val gridColumn = getGridColumn()

    LaunchedEffect(isTablet) {
        if (isTablet) {
            gameOfLifeViewModel.initCellularSpace(20, 80)
        } else {
            gameOfLifeViewModel.initCellularSpace(gridRow, gridColumn)
        }
    }

    // Game Loop logic
    val cellularSpace by gameOfLifeViewModel.cellularSpace.collectAsStateWithLifecycle()
    LaunchedEffect(buttonsViewModel.isRunning, cellularSpace) {
        if (buttonsViewModel.isRunning) {
            gameOfLifeViewModel.capturePreviousGrid()
            runGameLoop(
                updateCells = { gameOfLifeViewModel.updateCells(it) },
                addToCounter = { gameOfLifeViewModel.addToCounter() },
                cellularSpace = cellularSpace,
                speedFlow = gameOfLifeViewModel.speedState
            )
        }
    }

    Column(
        modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {

        //Game board
        val gridUiSize by gameOfLifeViewModel.gridSize.collectAsStateWithLifecycle()
        Board(
            isTablet = isTablet,
            gameUIState = gameUIState,
            onCellClick = gameOfLifeViewModel.onCellClick,
            gridUiSize = gridUiSize,
            gridChange = { gameOfLifeViewModel.modifyGridSize(it) }
        )


        val boardGridSize by gameOfLifeViewModel.gridSize.collectAsStateWithLifecycle()
        val patterns by patternViewModel.patterns.collectAsStateWithLifecycle()
        PatternsUI(
            boardGridSize = boardGridSize,
            patterns = patterns,
            onAddCustomPattern = { cells, text ->
                patternViewModel.addCustomPattern(
                    cells,
                    text
                )
            },
            onGetPatternById = patternViewModel::getPatternById,
            onRotatePattern = patternViewModel::rotatePattern,
            onTogglePatternSelection = buttonsViewModel::togglePatternSelection,
            isEditingMode = buttonsViewModel.isEditingMode,
            selectedPatternIds = buttonsViewModel.selectedPatternIds,
            currentGrid = gameUIState.colored,
            previousGrid = gameOfLifeViewModel.previousGrid,
            isTablet = isTablet,
            isGameRunning = buttonsViewModel.isRunning,
        )


        //Play pause button
        Buttons(
            aliveCells = gameUIState.colored,
            onResetGrid = { gameOfLifeViewModel.resetGrid()
                          if (buttonsViewModel.isRunning){
                              buttonsViewModel.togglePause()
                          }
                          },
            isEditingMode = buttonsViewModel.isEditingMode,
            isRunning = buttonsViewModel.isRunning,
            onToggleEditingMode = { buttonsViewModel.toggleEditingMode() },
            onTogglePause = { buttonsViewModel.togglePause() },
            onDeleteSelectedPatterns = {
                patternViewModel.deletePatterns(buttonsViewModel.selectedPatternIds.toList())
            }
        )


        Row(
            Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            SliderSpeed(
                modifier = Modifier.weight(1f),
                onPositionChange = { gameOfLifeViewModel.changeSpeedGeneration(it) }
            )

            IconButton(
                onClick = {
                    showOnboarding()
                }
            ) {
                Icon(
                    painter = painterResource(Res.drawable.info_24px),
                    contentDescription = "Info",
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            GenerationCounter(gameUIState)
        }
    }
}

@Composable
fun SliderSpeed(modifier: Modifier = Modifier, onPositionChange: (Float) -> Unit) {
    var sliderPosition by remember { mutableFloatStateOf(1f) }
    Column(modifier = modifier.padding(horizontal = 8.dp)) {
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
private fun GenerationCounter(gameUIState: GameUiState) {
    Surface(
        color = MaterialTheme.colorScheme.primaryContainer,
        contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
        shape = RoundedCornerShape(50),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.5f))
    ) {
        Text(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
            text = "Gen #${gameUIState.generationCounter}",
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold
        )
    }
}
