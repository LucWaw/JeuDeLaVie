package kmp.project.gameoflife.di

import androidx.room.RoomDatabase
import kmp.project.gameoflife.data.GameOfLifeDatabase
import kmp.project.gameoflife.data.repository.ThemeRepository
import kmp.project.gameoflife.data.repository.service.PatternRepository
import kmp.project.gameoflife.data.utils.DatabaseCallback
import kmp.project.gameoflife.domain.usecase.AddAPattern
import kmp.project.gameoflife.domain.usecase.DeletePattern
import kmp.project.gameoflife.domain.usecase.GetAllPatterns
import kmp.project.gameoflife.platformModule
import kmp.project.gameoflife.ui.GameOfLifeViewModel
import kmp.project.gameoflife.ui.game.ButtonsViewModel
import kmp.project.gameoflife.ui.pattern.MovablePatternViewModel
import kmp.project.gameoflife.ui.theme.ThemeViewModel
import kotlinx.coroutines.Dispatchers
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module


val appModule = module {
    single<GameOfLifeDatabase> {
        val builder = get<RoomDatabase.Builder<GameOfLifeDatabase>>()

        builder
            .setQueryCoroutineContext(Dispatchers.IO)
            .addCallback(DatabaseCallback)
            .build()
    }


    // Services / Repositories
    singleOf(::PatternRepository)
    singleOf(::ThemeRepository)

    // Use Cases (Logic)
    factoryOf(::AddAPattern)
    factoryOf(::DeletePattern)
    factoryOf(::GetAllPatterns)

    // ViewModels
    viewModelOf(::GameOfLifeViewModel)
    viewModelOf(::ButtonsViewModel)
    viewModelOf(::MovablePatternViewModel)
    viewModelOf(::ThemeViewModel)
}

fun initKoin(config: KoinAppDeclaration? = null) {
    startKoin {
        config?.invoke(this)
        modules(platformModule(), appModule)
    }
}
