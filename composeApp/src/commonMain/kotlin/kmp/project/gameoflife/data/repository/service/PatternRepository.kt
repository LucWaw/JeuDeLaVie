package kmp.project.gameoflife.data.repository.service

import kmp.project.gameoflife.data.service.PatternAPI
import kmp.project.gameoflife.ui.pattern.PatternUIState

class PatternRepository(private val patternAPI: PatternAPI) {


    fun addPattern(pattern: PatternUIState) {
    }

    fun getAllPatterns(): List<PatternUIState> {
        return patternAPI.getAllPatterns()
    }
}