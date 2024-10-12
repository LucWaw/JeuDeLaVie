package ui.pattern

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import ui.DragTarget


data class Pattern(
    val id: Int,
    val name: String,
    val gridSize: Int = 4,
    val cells: List<Pair<Int, Int>>
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
    pattern : Pattern,
    modifier: Modifier = Modifier
) {
    DragTarget(dataToDrop = pattern, modifier = Modifier) {
        LazyVerticalGrid(GridCells.Fixed(pattern.gridSize), modifier = modifier.aspectRatio(1f)) {
            items(pattern.gridSize * pattern.gridSize) {
                val cellCoord = Pair(it / pattern.gridSize, it % pattern.gridSize)
                Box(
                    modifier = modifier
                        .aspectRatio(1f)
                        .background(if (pattern.cells.contains(cellCoord)) Color.Black else Color.Transparent)
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

