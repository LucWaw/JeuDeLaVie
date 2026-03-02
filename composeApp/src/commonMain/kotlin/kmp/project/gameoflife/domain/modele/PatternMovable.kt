package kmp.project.gameoflife.domain.modele

import kmp.project.gameoflife.data.entity.Pattern
import kmp.project.gameoflife.data.utils.RleParser
import kotlinx.serialization.Serializable

@Serializable
enum class PatternType {
    STILL_LIFE, // Fixe
    MOVING,     // Mouvant (Oscillateurs ou Vaisseaux)
    CUSTOM      // Créé par l'utilisateur
}

@Serializable
data class PatternMovable(
    val id: Long = 0,
    val name: String = "",
    val gridSize: Int = 4,
    val cells: List<Pair<Int, Int>> = emptyList(),
    val type: PatternType = PatternType.STILL_LIFE
) {
    fun toEntity(): Pattern {
        return Pattern(
            id = id,
            rle = RleParser.encode(cells, name),
            type = type
        )
    }
}


fun Pattern.toDomain(): PatternMovable {
    return PatternMovable(
        id = id,
        name = RleParser.getName(this.rle) ?: "Unnamed",
        gridSize = RleParser.getGridSize(this.rle),
        cells = RleParser.decode(this.rle),
        type = this.type
    )
}