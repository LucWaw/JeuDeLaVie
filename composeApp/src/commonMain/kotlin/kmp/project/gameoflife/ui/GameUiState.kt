package kmp.project.gameoflife.ui

import androidx.compose.runtime.Composable
import kmp.project.gameoflife.getPlatform

val isDesktop: Boolean = getPlatform().name.startsWith("Java")


data class GameUiState(
    val colored: Set<Pair<Int, Int>> = emptySet(),
    val generationCounter : Int = 0,
    val speedGeneration : Float = 1f
)

@Composable
fun getGridRow(): Int {
    val gridRow = if (isDesktop) 20 else 100
    return gridRow
}

@Composable
fun getGridColumn(): Int {
    val gridColumn = if (isDesktop) 80 else 100
    return gridColumn
}
