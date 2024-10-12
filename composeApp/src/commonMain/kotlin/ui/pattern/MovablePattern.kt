package ui.pattern

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import ui.DragTarget


data class Pattern(
    val id: Int,
    val name: String,
    val gridSize: Int = 4,
    var cells: List<Pair<Int, Int>>
)//grid handled by cells

val patternList = listOf(
    Pattern(1, "Square", 4, listOf(Pair(0, 0), Pair(0, 1), Pair(1, 0), Pair(1, 1))),
    Pattern(
        2, "Glider", 5, listOf(Pair(1, 0), Pair(2, 1), Pair(0, 2), Pair(1, 2), Pair(2, 2))
    ),
    Pattern(
        3, "Blinker", 5, listOf(Pair(0, 0), Pair(1, 0), Pair(2, 0))
    ),
    Pattern(
        4, "Toad", 5, listOf(Pair(0, 0), Pair(1, 0), Pair(2, 0), Pair(0, 1), Pair(1, 1), Pair(2, 1))
    ),
    Pattern(
        5, "Beacon", 6,
        listOf(Pair(0, 0), Pair(1, 0), Pair(0, 1), Pair(3, 2), Pair(2, 3), Pair(3, 3))
    ),
)

@Composable
fun PatternUI(
    pattern: Pattern,
    modifier: Modifier = Modifier
) {
    val patternState = remember { mutableStateOf(pattern) }
    Column(modifier = modifier.fillMaxWidth()) {

        Button(
            modifier = modifier.width(100.dp),
            onClick = {
                // Tourner les cellules du pattern de 90 degrés dans le sens horaire
                val gridSize = patternState.value.gridSize
                val rotatedCells = patternState.value.cells.map { (x, y) ->
                    Pair(y, gridSize - 1 - x)
                }
                // Créer un nouvel objet Pattern avec les cellules mises à jour
                patternState.value = patternState.value.copy(cells = rotatedCells)
            }
        ) {
            Icon(
                imageVector = Icons.Filled.Refresh,
                contentDescription = "Rotate"
            )
        }

        DragTarget(dataToDrop = patternState.value, modifier = modifier) {
            LazyVerticalGrid(
                GridCells.Fixed(patternState.value.gridSize),
                modifier = modifier.aspectRatio(1f)
            ) {
                items(patternState.value.gridSize * patternState.value.gridSize) {
                    val cellCoord = Pair(it / patternState.value.gridSize, it % patternState.value.gridSize)
                    Box(
                        modifier = modifier
                            .aspectRatio(1f)
                            .background(
                                if (patternState.value.cells.contains(cellCoord)) Color.Black else Color.Transparent
                            )
                            .border(
                                if (patternState.value.cells.contains(cellCoord)) {
                                    BorderStroke(1.dp, Color.DarkGray)
                                } else BorderStroke(0.dp, Color.LightGray)
                            )
                    )
                }
            }
        }
    }
}
