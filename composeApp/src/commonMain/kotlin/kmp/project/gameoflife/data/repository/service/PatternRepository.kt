package kmp.project.gameoflife.data.repository.service

import kmp.project.gameoflife.data.GameOfLifeDatabase
import kmp.project.gameoflife.domain.modele.PatternMovable
import kmp.project.gameoflife.domain.modele.toDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PatternRepository(private val database: GameOfLifeDatabase) {
    suspend fun addPattern(pattern: PatternMovable) {
        database.patternDao().insert(pattern.toEntity())
    }

    val patterns: Flow<List<PatternMovable>> = database.patternDao().getAllPatterns().map { pattern ->
        pattern.map { it.toDomain() }
    }

    suspend fun deletePattern(pattern: PatternMovable) {
        database.patternDao().delete(pattern.toEntity())
    }

}