package kmp.project.gameoflife.data.service

import kmp.project.gameoflife.ui.pattern.PatternUIState

class PatternFakeAPI : PatternAPI {

    private val patternList = listOf(
        PatternUIState(1, "Square", 4, listOf(Pair(0, 0), Pair(0, 1), Pair(1, 0), Pair(1, 1))),
        PatternUIState(
            2, "Glider", 5, listOf(Pair(1, 0), Pair(2, 1), Pair(0, 2), Pair(1, 2), Pair(2, 2))
        ),
        PatternUIState(
            3, "Blinker", 5, listOf(Pair(0, 0), Pair(1, 0), Pair(2, 0))
        ),
        PatternUIState(
            4, "Toad", 5, listOf(Pair(0, 0), Pair(1, 0), Pair(2, 0), Pair(0, 1), Pair(1, 1), Pair(2, 1))
        ),
        PatternUIState(
            5, "Beacon", 6,
            listOf(Pair(0, 0), Pair(1, 0), Pair(0, 1), Pair(3, 2), Pair(2, 3), Pair(3, 3))
        ),
        PatternUIState(
            6, "Pulsar", 14,
            listOf(
                Pair(2, 0), Pair(3, 0), Pair(4, 0), Pair(8, 0), Pair(9, 0), Pair(10, 0),
                Pair(0, 2), Pair(5, 2), Pair(7, 2), Pair(12, 2),
                Pair(0, 3), Pair(5, 3), Pair(7, 3), Pair(12, 3),
                Pair(0, 4), Pair(5, 4), Pair(7, 4), Pair(12, 4),
                Pair(2, 5), Pair(3, 5), Pair(4, 5), Pair(8, 5), Pair(9, 5), Pair(10, 5),
                Pair(2, 7), Pair(3, 7), Pair(4, 7), Pair(8, 7), Pair(9, 7), Pair(10, 7),
                Pair(0, 8), Pair(5, 8), Pair(7, 8), Pair(12, 8),
                Pair(0, 9), Pair(5, 9), Pair(7, 9), Pair(12, 9),
                Pair(0, 10), Pair(5, 10), Pair(7, 10), Pair(12, 10),
                Pair(2, 12), Pair(3, 12), Pair(4, 12), Pair(8, 12), Pair(9, 12), Pair(10, 12)
            )
        ),
        PatternUIState(
            7, "Pentadecathlon", 10,
            listOf(
                Pair(1, 0), Pair(2, 0), Pair(3, 0), Pair(4, 0), Pair(5, 0), Pair(6, 0), Pair(7, 0), Pair(8, 0), Pair(9, 0),
                Pair(1, 1), Pair(2, 1), Pair(3, 1), Pair(4, 1), Pair(5, 1), Pair(6, 1), Pair(7, 1), Pair(8, 1), Pair(9, 1)
            )
        ),
        PatternUIState(
            id = 8, "Heavy-Weight Spaceship", gridSize = 7,
            listOf(
                Pair(0, 2), Pair(0, 3), //Première ligne
                Pair (1, 0), Pair(1, 5), //Deuxième ligne
                Pair(2, 6), //Troisième ligne
                Pair(3, 0), Pair(3, 6), //Quatrième ligne
                Pair(4, 1), Pair(4, 2), Pair(4, 3), Pair(4, 4), Pair(4, 5), Pair(4,6) //Cinquième ligne
            )
        )


    )

    override fun getAllPatterns(): List<PatternUIState> {
        return patternList
    }
}