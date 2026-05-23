package kmp.project.gameoflife.ui


data class GameUiState(
    val colored: Set<Pair<Int, Int>> = emptySet(),
    val generationCounter : Int = 0,
    val speedGeneration : Float = 1f
)
