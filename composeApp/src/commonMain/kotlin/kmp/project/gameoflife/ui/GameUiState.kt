package kmp.project.gameoflife.ui

import androidx.compose.runtime.Composable
import kmp.project.gameoflife.getPlatform

val isDesktop: Boolean = getPlatform().name.startsWith("Java")


data class GameUiState(
    val colored: List<Pair<Int, Int>> = emptyList(),
    val generationCounter : Int = 0,
    val speedGeneration : Float = 1f
)

@Composable
fun getGridRow(): Int {
    val gridRow = if (isDesktop) 20 else 15
    return gridRow
}

@Composable
fun getGridColumn(): Int {
    val gridColumn = if (isDesktop) 80 else 15
    return gridColumn
}

