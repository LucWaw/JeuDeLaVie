package kmp.project.gameoflife.di

import kmp.project.gameoflife.data.repository.service.PatternRepository
import kmp.project.gameoflife.data.service.PatternFakeAPI
import kmp.project.gameoflife.ui.GameOfLifeViewModel
import kmp.project.gameoflife.ui.game.ButtonsViewModel
import kmp.project.gameoflife.ui.pattern.MovablePatternViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    // Services / Repositories
    single { PatternFakeAPI() }
    singleOf(::PatternRepository)

    // ViewModels
    viewModelOf(::GameOfLifeViewModel)
    viewModelOf(::ButtonsViewModel)
    viewModelOf(::MovablePatternViewModel)
}
