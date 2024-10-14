package ui.pattern

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
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
import model.PatternUIState
import ui.DragTarget


//grid handled by cells



@Composable
fun PatternsUI(
) {
    val viewModel = remember { MovablePatternViewModel() }


    val patternsUiState by viewModel.patterns.collectAsState()

    LazyRow(
        modifier = Modifier.height(250.dp),
    ) {
        items(items = patternsUiState) { pattern ->
            Pattern(
                pattern = pattern,
                getPattern = {viewModel.getPatternById(pattern.id)},
                rotatePattern = {viewModel.rotatePattern(pattern.id)})
        }
    }


}

@Composable
fun Pattern(pattern: PatternUIState, rotatePattern: () -> Unit, getPattern: () -> PatternUIState?){


    Column(modifier = Modifier.fillMaxWidth()) {

        Button(
            modifier = Modifier.width(200.dp),
            onClick = rotatePattern

        ) {
            Icon(
                imageVector = Icons.Filled.Refresh,
                contentDescription = "Rotate"
            )
        }

        DragTarget(dataToDrop = getPattern, modifier = Modifier.width(200.dp)) {
            LazyVerticalGrid(
                GridCells.Fixed(pattern.gridSize),
                modifier = Modifier.aspectRatio(1f)
            ) {
                items(pattern.gridSize * pattern.gridSize) {
                    val cellCoord = Pair(it / pattern.gridSize, it % pattern.gridSize)
                    Box(
                        modifier = Modifier
                            .aspectRatio(1f)
                            .background(
                                if (pattern.cells.contains(cellCoord)) Color.Black else Color.Transparent
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
