package kmp.project.gameoflife.domain.usecase

import kmp.project.gameoflife.data.repository.service.PatternRepository
import kmp.project.gameoflife.domain.modele.PatternMovable

class DeletePattern (private val repo: PatternRepository) {
    suspend fun invoke(patternMovable: PatternMovable) {
        repo.deletePattern(patternMovable)
    }
}