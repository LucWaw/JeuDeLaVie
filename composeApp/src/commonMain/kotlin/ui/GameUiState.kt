package ui

data class GameUiState(
    val colored: List<Pair<Int, Int>>,
    val gridSize: Int = 15,
    val generationCounter : Int = 0
)

