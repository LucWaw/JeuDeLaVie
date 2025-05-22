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
        ),
        PatternUIState(
            id = 9, "Light-Weight Spaceship", gridSize = 6,
            listOf(
                Pair(0, 1), Pair(0, 4), //Première ligne
                Pair (1, 0), //Deuxième ligne
                Pair(2, 0), Pair(2, 4), //Quatrième ligne
                Pair(3, 0),Pair(3, 1), Pair(3, 2), Pair(3, 3) //Cinquième ligne
            )
        ),
        PatternUIState(
            id = 10, name = "T-tetromino", gridSize = 4,
            listOf(
                Pair(0,1),// Première ligne
                Pair(1,0), Pair(1,1), Pair(1,2) //Deuxième ligne
            )
        ),
        PatternUIState(
            id = 11,
            name = "Boat",
            gridSize = 4,
            cells = listOf(
                Pair(0, 1),
                Pair(1, 0),
                Pair(2, 1),
                Pair(0, 2),
                Pair(1, 2)
            )
        )



    )

    override fun getAllPatterns(): List<PatternUIState> {
        return patternList
    }
}