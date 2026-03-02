package kmp.project.gameoflife.data.service

import kmp.project.gameoflife.domain.modele.PatternMovable

interface PatternAPI {//TODO DELETE

    /**
     * Adds a pattern at the first place in the repository to make custom patterns more visible
     * @param pattern The pattern to add
     */
    fun addPattern(pattern: PatternMovable)

    fun getAllPatterns(): List<PatternMovable>
}