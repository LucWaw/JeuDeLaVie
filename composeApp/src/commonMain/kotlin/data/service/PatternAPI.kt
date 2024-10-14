package data.service

import model.PatternUIState

interface PatternAPI {
    fun getAllPatterns(): List<PatternUIState>
}