package kmp.project.gameoflife.data.utils

import kmp.project.gameoflife.data.entity.Pattern
import kmp.project.gameoflife.domain.modele.PatternType

object InitialData {
    val patterns = listOf(
        Pattern(
            id = 10,
            rle = $$"""
            #N Glider
            x = 3, y = 3, rule = B3/S23
            bob$2bo$3o!
        """.trimIndent(),
            type = PatternType.MOVING
        ),
        Pattern(
            id = 9,
            rle = """
            #N Blinker
            x = 3, y = 1, rule = B3/S23
            3o!
        """.trimIndent(),
            type = PatternType.MOVING
        ),
        Pattern(
            id = 8,
            rle = $$"""
            #N Toad
            x = 6, y = 2, rule = B3/S23
            b3o$3o!
        """.trimIndent(),
            type = PatternType.MOVING
        ),
        Pattern(
            id = 7,
            rle = $$"""
            #N Beacon
            x = 4, y = 4, rule = B3/S23
            2o2b$o3b$3bo$2b2o!
        """.trimIndent(),
            type = PatternType.MOVING
        ),
        Pattern(
            id = 6,
            rle = $$"""
            #N 2x9 Column
            x = 2, y = 9, rule = B3/S23
            9o$9o$!
        """.trimIndent(),
            type = PatternType.MOVING
        ),
        Pattern(
            id = 5,
            rle = $$"""
            #N Heavy-Weight Spaceship
            x = 5, y = 7, rule = B3/S23
            bobob$4bo$o3bo$o3bo$4bo$bo2bo$2b3o!
        """.trimIndent(),
            type = PatternType.MOVING
        ),
        Pattern(
            id = 4,
            rle = $$"""
            #N Light-Weight Spaceship
            x = 4, y = 5, rule = B3/S23
            obo$3bo$3bo$o2bo$b3o!
        """.trimIndent(),
            type = PatternType.MOVING
        ),
        Pattern(
            id = 3,
            rle = $$"""
            #N T-tetromino
            x = 3, y = 2, rule = B3/S23
            3o$bo!
        """.trimIndent(),
            type = PatternType.MOVING
        ),
        Pattern(
            id = 2,
            rle = $$"""
            #N Boat
            x = 3, y = 3, rule = B3/S23
            2ob$obo$bo!
        """.trimIndent(),
            type = PatternType.STILL_LIFE
        ),
        Pattern(
            id = 1,
            rle = $$"""
            #N Square
            x = 2, y = 2, rule = B3/S23
            2o$2o!
        """.trimIndent(),
            type = PatternType.STILL_LIFE
        )
    )
}
