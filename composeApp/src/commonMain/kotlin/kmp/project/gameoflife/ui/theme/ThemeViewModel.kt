package kmp.project.gameoflife.ui.theme

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kmp.project.gameoflife.data.repository.ThemeRepository
import kmp.project.gameoflife.data.repository.service.PatternRepository
import kmp.project.gameoflife.data.utils.InitialData
import kmp.project.gameoflife.di.ToastManager
import kmp.project.gameoflife.domain.modele.toDomain
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ThemeViewModel(
    private val repository: ThemeRepository,
    private val patternRepository: PatternRepository,
    private val toastManager: ToastManager
) : ViewModel() {

    val gridRows = repository.getGridRows().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = 20
    )

    private var _gridColumns: kotlinx.coroutines.flow.StateFlow<Int>? = null
    fun gridColumns(isTablet: Boolean): kotlinx.coroutines.flow.StateFlow<Int> {
        if (_gridColumns == null) {
            _gridColumns = repository.getGridColumns(isTablet).stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = if (isTablet || kmp.project.gameoflife.getPlatform().name.startsWith("Java")) 80 else 20
            )
        }
        return _gridColumns!!
    }

    fun updateGridRows(rows: Int) {
        viewModelScope.launch {
            repository.setGridRows(rows)
        }
    }

    fun updateGridColumns(columns: Int) {
        viewModelScope.launch {
            repository.setGridColumns(columns)
        }
    }


    val themeState = repository.theme.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = repository.defaultTheme
    )

    fun updateTheme(newTheme: ColorTheme) {
        viewModelScope.launch {
            repository.setTheme(newTheme)
        }
    }

    fun restoreBasePatterns(restoredMessage: String) {
        viewModelScope.launch {
            val currentPatterns = patternRepository.patterns.first()
            val currentIds = currentPatterns.map { it.id }.toSet()
            InitialData.patterns.forEach { basePattern ->
                if (!currentIds.contains(basePattern.id)) {
                    patternRepository.addPattern(basePattern.toDomain())
                }
            }
            toastManager.show(restoredMessage)
        }
    }
}
