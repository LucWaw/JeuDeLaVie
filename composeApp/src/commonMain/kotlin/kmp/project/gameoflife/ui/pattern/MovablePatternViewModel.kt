package kmp.project.gameoflife.ui.pattern

import androidx.lifecycle.ViewModel
import kmp.project.gameoflife.data.repository.service.PatternRepository
import kmp.project.gameoflife.data.service.PatternFakeAPI
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update


class MovablePatternViewModel: ViewModel()  {


    private val _patterns = MutableStateFlow(PatternRepository(PatternFakeAPI()).getAllPatterns())
    val patterns: StateFlow<List<PatternUIState>> = _patterns.asStateFlow()


    fun getPatternById(id: Int): PatternUIState? {
        return patterns.value.find { it.id == id }
    }

    /**
     * Tourner les cellules du pattern de 90 degrés dans le sens horaire
     *
     */
    fun rotatePattern(id : Int) {
        _patterns.value.find { it.id == id }?.let { pattern ->
            val newCells = pattern.cells.map { (y, x) ->
                Pair(x, pattern.gridSize - 1 - y)
            }
            _patterns.update { patterns ->
                patterns.map { pattern ->
                    if (pattern.id == id) {
                        pattern.copy(cells = newCells)
                    } else {
                        pattern
                    }
                }
            }
        }
    }
}