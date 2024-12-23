package kmp.project.gameoflife.ui.pattern

data class PatternUIState(
    val id: Int = 0,
    val name: String = "",
    val gridSize: Int = 4,
    val cells: List<Pair<Int, Int>> = emptyList()
)

