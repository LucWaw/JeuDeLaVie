package kmp.project.gameoflife.ui.pattern

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
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
import kmp.project.gameoflife.domain.modele.PatternMovable
import kmp.project.gameoflife.domain.modele.PatternType
import kmp.project.gameoflife.showToast
import kmp.project.gameoflife.ui.draganddrop.CustomDragTarget
import kmp.project.gameoflife.ui.getGridColumn
import kmp.project.gameoflife.ui.getGridRow
import kmp.project.gameoflife.ui.theme.Shapes
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource


@Composable
fun PatternsUI(
    modifier: Modifier = Modifier,
    boardGridSize: Size,
    patterns: List<PatternMovable>,
    onAddCustomPattern: (Set<Pair<Int, Int>>, String) -> Unit,
    onGetPatternById: (Long) -> PatternMovable?,
    onRotatePattern: (Long) -> Unit,
    onTogglePatternSelection: (Long) -> Unit,
    isEditingMode: Boolean,
    selectedPatternIds: List<Long>,
    currentGrid: Set<Pair<Int, Int>> = emptySet(),
    previousGrid: Set<Pair<Int, Int>> = emptySet(),
    isTablet: Boolean = false,
    isGameRunning: Boolean = false,
) {
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
        val customPatternCurrentShortSaved = stringResource(
            Res.string.custom_pattern_saved,
            stringResource(Res.string.current_grid_short)
        )
        val customPatternPreviousShortSaved = stringResource(
            Res.string.custom_pattern_saved,
            stringResource(Res.string.previous_grid_short)
        )

        when {
            currentGrid.isEmpty() && previousGrid.isEmpty() -> {
                onAddCustomPattern(
                    emptySet(),
                    customPatternEmptyError
                ) // Déclenchera le toast "vide"
                showGridCustomPatternDialog = false 
            }

            currentGrid.isNotEmpty() && previousGrid.isEmpty() -> {
                onAddCustomPattern(currentGrid, customPatternCurrentShortSaved)
                showGridCustomPatternDialog = false
            }

            currentGrid.isEmpty() && previousGrid.isNotEmpty() -> { 
                onAddCustomPattern(previousGrid, customPatternPreviousShortSaved)
                showGridCustomPatternDialog = false
            }

            else -> {
                // Les deux sont remplies, on affiche le dialogue
                SelectGridForCustomPatternDialogCustom(
                    onDismissRequest = {
                        showGridCustomPatternDialog = false 
                    },
                    onConfirmCurrentGridPattern = {
                        onAddCustomPattern(
                            currentGrid,
                            customPatternCurrentShortSaved
                        )
                    },
                    onConfirmPreviousGridPattern = {
                        onAddCustomPattern(
                            previousGrid,
                            customPatternPreviousShortSaved
                        )
                    }
                )
            }
        }
    }

    BoxWithConstraints(modifier = modifier.fillMaxWidth()) {
        val screenHeight = maxHeight
        val rowHeight = 130.dp

        // Si l'écran est assez haut, autorise 2 lignes, sinon 1
        val visibleRows = if (screenHeight > 400.dp && maxWidth < 600.dp) 2 else 1
        val gridHeight = rowHeight * visibleRows

        LazyHorizontalGrid(
            rows = GridCells.Fixed(visibleRows),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.height(gridHeight).padding(top = 4.dp, start = 8.dp, end = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            item {
                val runningText = stringResource(Res.string.cant_add_pattern_while_game_running)
                Button(
                    modifier = Modifier
                        .width(rowHeight - 50.dp)
                        .fillMaxHeight()
                        .border(
                            BorderStroke(
                                width = 2.dp,
                                color = MaterialTheme.colorScheme.tertiary
                            ),
                            MaterialTheme.shapes.medium
                        ),
                    shape = MaterialTheme.shapes.medium,
                    onClick = {
                        if (!isGameRunning) {
                            showGridCustomPatternDialog = true
                        } else {
                            showToast(runningText)
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                        contentColor = MaterialTheme.colorScheme.tertiary
                    )
                ) {
                    Icon(
                        modifier = Modifier.size(32.dp),
                        painter = painterResource(Res.drawable.add_24px),
                        contentDescription = "Add a pattern",
                    )
                }
            }
            items(patterns.size) { index ->
                val pattern = patterns[index]
                val isSelected = selectedPatternIds.contains(pattern.id)

                Pattern(
                    modifier = Modifier.width(rowHeight - 50.dp).fillMaxHeight(),
                    pattern = pattern,
                    getPattern = { onGetPatternById(pattern.id) },
                    rotatePattern = { onRotatePattern(pattern.id) },
                    tileSize = tileSize,
                    isEditingMode = isEditingMode,
                    isSelected = isSelected,
                    onSelect = { onTogglePatternSelection(pattern.id) }
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
    Dialog(onDismissRequest = onDismissRequest) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier
                .fillMaxWidth(),
            color = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
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
        Text(text = title, style = MaterialTheme.typography.titleLarge)
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

        Column(Modifier.selectableGroup(), verticalArrangement = Arrangement.spacedBy(20.dp)) {
            gridRadioOptions.forEach { options ->
                Row(
                    Modifier
                        .fillMaxWidth()
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
                    Text(
                        text = options.name,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(start = 16.dp)
                    )
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
    pattern: PatternMovable,
    rotatePattern: () -> Unit,
    getPattern: () -> PatternMovable?,
    modifier: Modifier = Modifier,
    tileSize: Size = Size(20f, 20f),
    isEditingMode: Boolean = false,
    isSelected: Boolean = false,
    onSelect: () -> Unit = {}
) {

    val patternColor = when (pattern.type) {
        PatternType.STILL_LIFE -> MaterialTheme.colorScheme.outline
        PatternType.MOVING -> MaterialTheme.colorScheme.secondary
        PatternType.CUSTOM -> MaterialTheme.colorScheme.tertiary
    }

    Box(modifier = modifier.clickable(enabled = isEditingMode) { onSelect() }) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium,
                border = BorderStroke(
                    width = if (isSelected) 2.dp else 1.dp,
                    color = if (isSelected) MaterialTheme.colorScheme.error else patternColor
                ),
                color = MaterialTheme.colorScheme.surfaceVariant,
                contentColor = if (isEditingMode) patternColor.copy(alpha = 0.5f) else patternColor
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .then(
                            if (!isEditingMode) {
                                Modifier.clickable { rotatePattern() }
                            } else {
                                Modifier
                            }
                        )
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.rotate_90_degrees_cw_24px),
                        contentDescription = "Rotate"
                    )
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .background(MaterialTheme.colorScheme.surfaceVariant, Shapes.medium)
                    .border(
                        BorderStroke(
                            width = if (isSelected) 3.dp else 1.dp,
                            color = if (isSelected) MaterialTheme.colorScheme.error else patternColor.copy(alpha = 0.5f)
                        ),
                        Shapes.medium
                    )
                    .padding(4.dp)
            ) {
                CustomDragTarget(
                    data = getPattern,
                    modifier = Modifier,
                    gridSize = pattern.gridSize,
                    tileSize = tileSize,
                    isEnabled = !isEditingMode,
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
                                        if (isAlive) patternColor else Color.Transparent
                                    )
                                    .border(
                                        BorderStroke(
                                            width = 0.4.dp,
                                            color = MaterialTheme.colorScheme.outlineVariant
                                        )
                                    )
                            )
                        }
                    }
                }
            }
        }

        if (isEditingMode) {
            Checkbox(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(4.dp)
                    .size(20.dp),
                checked = isSelected,
                onCheckedChange = { onSelect() },
                colors = CheckboxDefaults.colors(
                    checkedColor = MaterialTheme.colorScheme.error
                )
            )
        }
    }
}
