package ui

data class GameUiState(
    val colored: List<Pair<Int, Int>>,
    val gridRow: Int = 20,
    val gridColumn: Int = 80,
    val numberOfCells: Int = gridRow * gridColumn,
    val generationCounter : Int = 0
)

