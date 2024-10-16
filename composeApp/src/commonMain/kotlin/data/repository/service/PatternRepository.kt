package data.repository.service

import data.service.PatternAPI
import ui.pattern.PatternUIState

class PatternRepository(private val patternAPI: PatternAPI) {


    fun getAllPatterns(): List<PatternUIState> {
        return patternAPI.getAllPatterns()
    }
}