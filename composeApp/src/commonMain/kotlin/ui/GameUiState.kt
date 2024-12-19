package ui

import getPlatform

val isDesktop: Boolean = getPlatform().name.startsWith("Java")

data class GameUiState(
    val colored: List<Pair<Int, Int>>,
    val gridRow: Int = if (isDesktop) 20 else 15,
    val gridColumn: Int = if (isDesktop) 80 else 15,
    val numberOfCells: Int = gridRow * gridColumn,
    val generationCounter : Int = 0
)

