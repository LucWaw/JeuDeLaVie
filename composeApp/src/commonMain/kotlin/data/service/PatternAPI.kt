package data.service

import ui.pattern.PatternUIState

interface PatternAPI {
    fun getAllPatterns(): List<PatternUIState>
}