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
import data.MiniState
import ui.DragTarget
import ui.LongPressDraggable


/*@Composable
fun listOfPattern() {
    DragTarget(){
    patternSquare()
    }
}*/
@Composable
fun patternSquare() {

    val miniStateBundle = MiniState(listOf(Pair(1, 1), Pair(1, 2), Pair(2, 2), Pair(2, 1)))

    //when drag via https://blog.canopas.com/android-drag-and-drop-ui-element-in-jetpack-compose-14922073b3f1

    val miniGridSize = 4


    LongPressDraggable(modifier = Modifier.height(100.dp).aspectRatio(1f).zIndex(1f)) {
        DragTarget(
            modifier = Modifier,
            dataToDrop = miniStateBundle
        ) {
            LazyVerticalGrid(GridCells.Fixed(miniGridSize)) {
                items(miniGridSize * miniGridSize) {
                    val cellCoord = Pair(it / miniGridSize, it % miniGridSize)
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

