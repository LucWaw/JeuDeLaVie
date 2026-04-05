package kmp.project.gameoflife.ui.theme

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kmp.project.gameoflife.data.repository.ThemeRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ThemeViewModel(private val repository: ThemeRepository): ViewModel() {
    
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
}
