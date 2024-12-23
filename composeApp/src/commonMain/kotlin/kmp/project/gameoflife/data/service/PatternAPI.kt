package kmp.project.gameoflife.data.service

import kmp.project.gameoflife.ui.pattern.PatternUIState

interface PatternAPI {
    fun getAllPatterns(): List<PatternUIState>
}