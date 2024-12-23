package kmp.project.gameoflife.data.repository.service

import kmp.project.gameoflife.data.service.PatternAPI
import kmp.project.gameoflife.ui.pattern.PatternUIState

class PatternRepository(private val patternAPI: PatternAPI) {


    fun getAllPatterns(): List<PatternUIState> {
        return patternAPI.getAllPatterns()
    }
}