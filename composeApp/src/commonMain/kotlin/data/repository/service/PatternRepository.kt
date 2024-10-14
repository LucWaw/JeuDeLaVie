package data.repository.service

import data.service.PatternAPI
import model.PatternUIState

class PatternRepository(private val patternAPI: PatternAPI) {


    fun getAllPatterns(): List<PatternUIState> {
        return patternAPI.getAllPatterns()
    }
}