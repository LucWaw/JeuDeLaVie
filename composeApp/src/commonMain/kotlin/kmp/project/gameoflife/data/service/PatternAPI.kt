package kmp.project.gameoflife.data.service

import kmp.project.gameoflife.ui.pattern.PatternUIState

interface PatternAPI {

    /**
     * Adds a pattern at the first place in the repository to make custom patterns more visible
     * @param pattern The pattern to add
     */
    fun addPattern(pattern: PatternUIState)

    fun getAllPatterns(): List<PatternUIState>
}