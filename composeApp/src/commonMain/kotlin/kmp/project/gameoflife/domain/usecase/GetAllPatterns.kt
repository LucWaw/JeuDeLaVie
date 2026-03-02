package kmp.project.gameoflife.domain.usecase

import kmp.project.gameoflife.data.repository.service.PatternRepository


class GetAllPatterns ( private val repo: PatternRepository){
    operator fun invoke() = repo.patterns
}