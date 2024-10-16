package data.service

import ui.pattern.PatternUIState

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
    )

    override fun getAllPatterns(): List<PatternUIState> {
        return patternList
    }
}