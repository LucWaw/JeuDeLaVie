package kmp.project.gameoflife.ui.pattern

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kmp.project.gameoflife.ui.draganddrop.DragTarget


//grid handled by cells


@Composable
fun PatternsUI() {
    val viewModel = remember { MovablePatternViewModel() }
    val patternsUiState by viewModel.patterns.collectAsState()

    BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
        val screenHeight = maxHeight
        val rowHeight = 130.dp

        // Si l'écran est assez haut, autorise 2 lignes, sinon 1
        val visibleRows = if (screenHeight > 400.dp && maxWidth < 600.dp) 2 else 1
        val gridHeight = rowHeight * visibleRows

        LazyHorizontalGrid(
            rows = GridCells.Fixed(visibleRows),
            modifier = Modifier
                .height(gridHeight),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(patternsUiState.size) { index ->
                val pattern = patternsUiState[index]
                Pattern(
                    modifier = Modifier
                        .width(rowHeight-50.dp),
                    pattern = pattern,
                    getPattern = { viewModel.getPatternById(pattern.id) },
                    rotatePattern = { viewModel.rotatePattern(pattern.id) }
                )
            }
        }
    }
}


@Composable
fun Pattern(pattern: PatternUIState, rotatePattern: () -> Unit, getPattern: () -> PatternUIState?, modifier: Modifier = Modifier) {
    val isInDark = isSystemInDarkTheme()


    Column(
        modifier = modifier
            //.width(patternWidth) // Imposer la même largeur
    ) {
        Button(
            modifier = Modifier.fillMaxWidth(), // Va prendre toute la largeur imposée par Column
            onClick = rotatePattern
        ) {
            Icon(
                imageVector = Icons.Filled.Refresh,
                contentDescription = "Rotate"
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth() // Même largeur que Button
                .aspectRatio(1f) // Pour garder un carré
        ) {
            DragTarget(dataToDrop = getPattern, modifier = Modifier) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(pattern.gridSize),
                    modifier = Modifier.fillMaxSize() // Remplit la Box carrée
                ) {
                    items(pattern.gridSize * pattern.gridSize) {
                        val cellCoord = Pair(it / pattern.gridSize, it % pattern.gridSize)
                        Box(
                            modifier = Modifier
                                .aspectRatio(1f)
                                .background(
                                    if (pattern.cells.contains(cellCoord)) {
                                        if (isInDark) Color.White else Color.Black
                                    } else Color.Transparent
                                )
                                .border(
                                    if (pattern.cells.contains(cellCoord)) {
                                        BorderStroke(1.dp, Color.DarkGray)
                                    } else BorderStroke(0.dp, Color.LightGray)
                                )
                        )
                    }
                }
            }
        }
    }
}
