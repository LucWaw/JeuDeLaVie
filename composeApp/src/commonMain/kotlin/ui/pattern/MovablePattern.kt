package ui.pattern

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import ui.DragTarget
import ui.LongPressDraggable


data class Pattern(
    val id: Int,
    val name: String,
    val grisSize: Int = 4,
    val cells: List<Pair<Int, Int>>
)//grid handled by cells

val foodList = listOf(
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
fun ListOfPattern(
    gridSize: Int = 4,
    cells: List<Pair<Int, Int>>,
    modifier: Modifier = Modifier
) {

    val miniStateBundle = cells

    //when drag via https://blog.canopas.com/android-drag-and-drop-ui-element-in-jetpack-compose-14922073b3f1


    LongPressDraggable(modifier = Modifier.height(100.dp).aspectRatio(1f).zIndex(1f)) {
        DragTarget(
            modifier = Modifier,
            dataToDrop = miniStateBundle
        ) {
            LazyVerticalGrid(GridCells.Fixed(gridSize)) {
                items(gridSize * gridSize) {
                    val cellCoord = Pair(it / gridSize, it % gridSize)
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .aspectRatio(1f)
                            .background(
                                if (miniStateBundle.colored.contains(cellCoord)) Color.Black else Color.Transparent
                            )
                            .border(
                                if (miniStateBundle.colored.contains(cellCoord)) {
                                    BorderStroke(1.dp, Color.DarkGray)
                                } else BorderStroke(0.dp, Color.LightGray)
                            )
                    )
                }
            }
        }
    }


}

