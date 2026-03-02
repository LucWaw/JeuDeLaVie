package kmp.project.gameoflife.data.service

import kmp.project.gameoflife.data.utils.RleParser
import kmp.project.gameoflife.domain.modele.PatternType
import kmp.project.gameoflife.domain.modele.PatternMovable

class PatternFakeAPI : PatternAPI {

    private val patternList = listOf(
        createPattern(
            1, $$"""
            #N Glider
            x = 3, y = 3, rule = B3/S23
            bob$2bo$3o!
        """.trimIndent(),
            PatternType.MOVING
        ),
        createPattern(
            2, """
            #N Blinker
            x = 3, y = 1, rule = B3/S23
            3o!
        """.trimIndent(),
            PatternType.MOVING
        ),
        createPattern(
            3, $$"""
            #N Toad
            x = 6, y = 2, rule = B3/S23
            b3o$3o!
        """.trimIndent(),
            PatternType.MOVING
        ),
        createPattern(
            4, $$"""
            #N Beacon
            x = 4, y = 4, rule = B3/S23
            2o2b$o3b$3bo$2b2o!
        """.trimIndent(),
            PatternType.MOVING
        ),
        createPattern(
            5, $$"""
            #N Pentadecathlon
            x = 10, y = 3, rule = B3/S23
            2bo4bo2b$2ob4ob2o$2bo4bo!
        """.trimIndent(),
            PatternType.MOVING
        ),
        createPattern(
            6, $$"""
            #N Heavy-Weight Spaceship
            x = 5, y = 7, rule = B3/S23
            bobob$4bo$o3bo$o3bo$4bo$bo2bo$2b3o!
        """.trimIndent(),
            PatternType.MOVING
        ),
        createPattern(
            7, $$"""
            #N Light-Weight Spaceship
            x = 4, y = 5, rule = B3/S23
            obo$3bo$3bo$o2bo$b3o!
        """.trimIndent(),
            PatternType.MOVING
        ),
        createPattern(
            8, $$"""
            #N T-tetromino
            x = 3, y = 2, rule = B3/S23
            3o$bo!
        """.trimIndent(),
            PatternType.MOVING
        ),
        createPattern(
            9, $$"""
            #N Boat
            x = 3, y = 3, rule = B3/S23
            2ob$obo$bo!
        """.trimIndent(),
            PatternType.STILL_LIFE
        ),
        createPattern(
            10, $$"""
            #N Square
            x = 2, y = 2, rule = B3/S23
            2o$2o!
        """.trimIndent(),
            PatternType.STILL_LIFE
        )
    )

    private fun createPattern(id: Long, rleWithMetadata: String, patternType: PatternType): PatternMovable {//TODO DELETE FROM HERE
        return PatternMovable(
            id = id,
            name = RleParser.getName(rleWithMetadata) ?: "Unnamed",
            gridSize = RleParser.getGridSize(rleWithMetadata),
            cells = RleParser.decode(rleWithMetadata),
            type = patternType
        )
    }

    override fun addPattern(pattern: PatternMovable) {
        //Should be added in first
    }

    override fun getAllPatterns(): List<PatternMovable> {
        return patternList
    }
}
