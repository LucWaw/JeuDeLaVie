package kmp.project.gameoflife.ui.pattern

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.RadioButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import gameoflife.composeapp.generated.resources.Res
import gameoflife.composeapp.generated.resources.add_24px
import gameoflife.composeapp.generated.resources.cant_add_pattern_while_game_running
import gameoflife.composeapp.generated.resources.current_grid_short
import gameoflife.composeapp.generated.resources.custom_pattern
import gameoflife.composeapp.generated.resources.custom_pattern_current_grid
import gameoflife.composeapp.generated.resources.custom_pattern_empty_error
import gameoflife.composeapp.generated.resources.custom_pattern_previous_grid
import gameoflife.composeapp.generated.resources.custom_pattern_saved
import gameoflife.composeapp.generated.resources.no
import gameoflife.composeapp.generated.resources.previous_grid_short
import gameoflife.composeapp.generated.resources.rotate_90_degrees_cw_24px
import gameoflife.composeapp.generated.resources.yes
import kmp.project.gameoflife.showToast
import kmp.project.gameoflife.ui.draganddrop.CustomDragTarget
import kmp.project.gameoflife.ui.draganddrop.Shapes
import kmp.project.gameoflife.ui.getGridColumn
import kmp.project.gameoflife.ui.getGridRow
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource


@Composable
fun PatternsUI(
    boardGridSize: Size,
    currentGrid: List<Pair<Int, Int>> = emptyList(),
    previousGrid: List<Pair<Int, Int>> = emptyList(),
    isTablet: Boolean = false,
    isGameRunning: Boolean = false
) {
    val viewModel = remember { MovablePatternViewModel() }
    val patternsUiState by viewModel.patterns.collectAsState()

    val gridRow = if (isTablet) 20 else getGridRow()
    val gridColumn = if (isTablet) 80 else getGridColumn()

    val tileSize = if (boardGridSize != Size.Zero && gridColumn > 0 && gridRow > 0) {
        Size(boardGridSize.width / gridColumn, boardGridSize.height / gridRow)
    } else {
        Size(20f, 20f) // Fallback
    }

    var showGridCustomPatternDialog by remember { mutableStateOf(false) }

    if (showGridCustomPatternDialog) {
        // Logique de sélection automatique ou manuelle
        val customPatternEmptyError = stringResource(Res.string.custom_pattern_empty_error)
        val customPatternCurrentShortSaved = stringResource(Res.string.custom_pattern_saved, stringResource(Res.string.current_grid_short))
        val customPatternPreviousShortSaved = stringResource(Res.string.custom_pattern_saved, stringResource(Res.string.previous_grid_short))

        when {
            currentGrid.isEmpty() && previousGrid.isEmpty() -> {
                viewModel.addCustomPattern(emptyList(), customPatternEmptyError) // Déclenchera le toast "vide"
                showGridCustomPatternDialog = false //Never read but useful for remember
            }
            currentGrid.isNotEmpty() && previousGrid.isEmpty() -> {
                viewModel.addCustomPattern(currentGrid, doneText = customPatternCurrentShortSaved)
                showGridCustomPatternDialog = false//Never read but useful for remember
            }
            currentGrid.isEmpty() && previousGrid.isNotEmpty() -> { //Not always true when reached
                viewModel.addCustomPattern(previousGrid, doneText = customPatternPreviousShortSaved)
                showGridCustomPatternDialog = false//Never read but useful for remember
            }
            else -> {
                // Les deux sont remplies, on affiche le dialogue
                SelectGridForCustomPatternDialogCustom(
                    onDismissRequest = { showGridCustomPatternDialog = false },//Never read but useful for remember
                    onConfirmCurrentGridPattern = { viewModel.addCustomPattern(currentGrid, doneText = customPatternCurrentShortSaved) },
                    onConfirmPreviousGridPattern = { viewModel.addCustomPattern(previousGrid, doneText = customPatternPreviousShortSaved) }
                )
            }
        }
    }

    BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
        val screenHeight = maxHeight
        val rowHeight = 130.dp

        // Si l'écran est assez haut, autorise 2 lignes, sinon 1
        val visibleRows = if (screenHeight > 400.dp && maxWidth < 600.dp) 2 else 1
        val gridHeight = rowHeight * visibleRows

        LazyHorizontalGrid(
            rows = GridCells.Fixed(visibleRows),
            modifier = Modifier.height(gridHeight),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            item {
                val runningText = stringResource(Res.string.cant_add_pattern_while_game_running)
                Button(
                    modifier = Modifier
                        .width(rowHeight - 50.dp)
                        .padding(7.dp)
                        .border(2.dp, Color(0xFF9C27B0), Shapes.medium), // Couleur Custom pour le bouton d'ajout
                    onClick = {
                        if (!isGameRunning) {
                            showGridCustomPatternDialog = true
                        } else {
                            showToast(runningText)
                        }},//Never read but useful for remember
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.White)
                ) {
                    Icon(
                        modifier = Modifier.fillMaxSize(),
                        painter = painterResource(Res.drawable.add_24px),
                        contentDescription = "Add a pattern",
                        tint = Color(0xFF9C27B0)
                    )
                }
            }
            items(patternsUiState.size) { index ->
                val pattern = patternsUiState[index]
                Pattern(
                    modifier = Modifier.width(rowHeight - 50.dp),
                    pattern = pattern,
                    getPattern = { viewModel.getPatternById(pattern.id) },
                    rotatePattern = { viewModel.rotatePattern(pattern.id) },
                    tileSize = tileSize
                )
            }
        }
    }
}

@Composable
fun SelectGridForCustomPatternDialogCustom(
    onDismissRequest: () -> Unit,
    onConfirmCurrentGridPattern: () -> Unit,
    onConfirmPreviousGridPattern: () -> Unit
) {
    val isInDark = isSystemInDarkTheme()
    Dialog(onDismissRequest = onDismissRequest) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            color = if (isInDark) Color(0xFF303030) else Color.White,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            GridDialogContent(
                onDismissRequest,
                onConfirmCurrentGridPattern,
                onConfirmPreviousGridPattern,
                stringResource(Res.string.custom_pattern)
            )
        }
    }
}


@Composable
fun GridDialogContent(
    onDismissRequest: () -> Unit,
    onConfirmCurrentGridPattern: () -> Unit,
    onConfirmPreviousGridPattern: () -> Unit,
    title: String
) {

    Column(
        modifier = Modifier
            .padding(24.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(text = title)
        val previousGrid = stringResource(Res.string.custom_pattern_previous_grid)
        val currentGrid = stringResource(Res.string.custom_pattern_current_grid)


        data class GridRadioOptions(
            val name: String,
            val method: () -> Unit
        )

        val gridRadioOptions = listOf(
            GridRadioOptions(currentGrid) { onConfirmCurrentGridPattern() },
            GridRadioOptions(previousGrid) { onConfirmPreviousGridPattern() }
        )

        val (selectedOption, onOptionSelected) = remember { mutableStateOf(gridRadioOptions[0].name) }

        Column(Modifier.selectableGroup()) {
            gridRadioOptions.forEach { options ->
                Row(
                    Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .selectable(
                            selected = (options.name == selectedOption),
                            onClick = { onOptionSelected(options.name) },
                            role = Role.RadioButton
                        )
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = (options.name == selectedOption),
                        onClick = { onOptionSelected(options.name) }
                    )
                    Text(text = options.name)
                }
            }
        }

        Row(
            horizontalArrangement = Arrangement.End,
            modifier = Modifier.fillMaxWidth()
        ) {
            TextButton(onClick = onDismissRequest) {
                Text(text = stringResource(resource = Res.string.no))
            }
            Spacer(modifier = Modifier.width(8.dp))
            TextButton(onClick = {
                val selected = gridRadioOptions.find { it.name == selectedOption }
                selected?.method?.invoke()
                onDismissRequest()
            }) {
                Text(text = stringResource(resource = Res.string.yes))
            }
        }
    }
}


@Composable
fun Pattern(
    pattern: PatternUIState,
    rotatePattern: () -> Unit,
    getPattern: () -> PatternUIState?,
    modifier: Modifier = Modifier,
    tileSize: Size = Size(20f, 20f)
) {
    val isInDark = isSystemInDarkTheme()

    val patternColor = when (pattern.type) {
        PatternType.STILL_LIFE -> Color(0xFF2196F3) // Blue
        PatternType.MOVING -> Color(0xFFFF9800)     // Orange
        PatternType.CUSTOM -> Color(0xFF9C27B0)     // Purple
    }

    Column(
        modifier = modifier
    ) {
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = rotatePattern,
            border = BorderStroke(1.dp, patternColor),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = if (isInDark) Color(0xFF303030) else Color.White,
                contentColor = patternColor
            )
        ) {
            Icon(
                painter = painterResource(Res.drawable.rotate_90_degrees_cw_24px),
                contentDescription = "Rotate"
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .border(BorderStroke(2.dp, patternColor), Shapes.medium)
                .padding(4.dp)
        ) {
            CustomDragTarget(
                data = getPattern,
                modifier = Modifier,
                gridSize = pattern.gridSize,
                tileSize = tileSize
            ) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(pattern.gridSize),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(
                        pattern.gridSize * pattern.gridSize,
                        key = { index ->
                            val coord = Pair(index / pattern.gridSize, index % pattern.gridSize)
                            "${pattern.id}_${index}_${pattern.cells.contains(coord)}"
                        }
                    ) {
                        val cellCoord = Pair(it / pattern.gridSize, it % pattern.gridSize)
                        val isAlive = pattern.cells.contains(cellCoord)
                        
                        Box(
                            modifier = Modifier
                                .aspectRatio(1f)
                                .background(
                                    if (isAlive) patternColor else patternColor.copy(alpha = 0.03f)
                                )
                                .border(
                                    BorderStroke(
                                        width = 0.5.dp, 
                                        color = if (isAlive) Color.White.copy(alpha = 0.4f) else patternColor.copy(alpha = 0.1f)
                                    )
                                )
                        )
                    }
                }
            }
        }
    }
}
