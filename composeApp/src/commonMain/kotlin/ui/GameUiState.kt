package ui

data class GameUiState(
    val colored: List<Pair<Int, Int>>,
    val gridRow: Int = 30,
    val gridColumn: Int = 100,
    val generationCounter : Int = 0
)

