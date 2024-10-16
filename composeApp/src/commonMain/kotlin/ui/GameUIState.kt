package ui

data class GameUIState(
    val colored: List<Pair<Int, Int>>,
    val gridSize: Int = 15
)

