package ui

data class GameUiState(
    val colored: List<Pair<Int, Int>>,
    val gridSize: Int = 15
)

