package kmp.project.gameoflife.domain.usecase

import kmp.project.gameoflife.data.repository.service.PatternRepository
import kmp.project.gameoflife.domain.modele.PatternMovable

class AddAPattern(private val repo: PatternRepository) {
    suspend fun invoke(pattern: PatternMovable) {
        repo.addPattern(pattern)
    }
}