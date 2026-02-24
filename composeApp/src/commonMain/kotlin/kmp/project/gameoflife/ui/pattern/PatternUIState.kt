package kmp.project.gameoflife.ui.pattern

import kotlinx.serialization.Serializable

@Serializable
data class PatternUIState(
    val id: Int = 0,
    val name: String = "",
    val gridSize: Int = 4,
    val cells: List<Pair<Int, Int>> = emptyList()
)

