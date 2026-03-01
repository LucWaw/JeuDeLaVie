package kmp.project.gameoflife.ui.pattern

import kotlinx.serialization.Serializable

@Serializable
enum class PatternType {
    STILL_LIFE, // Fixe
    MOVING,     // Mouvant (Oscillateurs ou Vaisseaux)
    CUSTOM      // Créé par l'utilisateur
}

@Serializable
data class PatternUIState(
    val id: Int = 0,
    val name: String = "",
    val gridSize: Int = 4,
    val cells: List<Pair<Int, Int>> = emptyList(),
    val type: PatternType = PatternType.STILL_LIFE
)
