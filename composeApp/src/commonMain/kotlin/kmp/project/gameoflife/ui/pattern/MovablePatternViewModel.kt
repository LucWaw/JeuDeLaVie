package kmp.project.gameoflife.ui.pattern

import androidx.lifecycle.ViewModel
import kmp.project.gameoflife.data.repository.service.PatternRepository
import kmp.project.gameoflife.data.service.PatternFakeAPI
import kmp.project.gameoflife.data.utils.RleParser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update


class MovablePatternViewModel: ViewModel()  {


    private val _patterns = MutableStateFlow(PatternRepository(PatternFakeAPI()).getAllPatterns().toMutableList())
    val patterns: StateFlow<List<PatternUIState>> = _patterns.asStateFlow()


    fun getPatternById(id: Int): PatternUIState? {
        return patterns.value.find { it.id == id }
    }

    /**
     * Tourner les cellules du pattern de 90 degrés dans le sens horaire
     *
     */
    fun rotatePattern(id : Int) {
        _patterns.update { patterns ->
            patterns.map { pattern ->
                if (pattern.id == id) {
                    val newCells = pattern.cells.map { (y, x) ->
                        Pair(x, pattern.gridSize - 1 - y)
                    }
                    pattern.copy(cells = newCells)
                } else {
                    pattern
                }
            }.toMutableList()
        }
    }

    /**
     * Ajoute un nouveau pattern personnalisé à partir d'une liste de cellules.
     */
    fun addCustomPattern(cells: List<Pair<Int, Int>>, name: String = "Custom Pattern") {
        if (cells.isEmpty()) return

        val rle = RleParser.encode(cells, name)
        val newId = (_patterns.value.maxOfOrNull { it.id } ?: 0) + 1
        
        val newPattern = PatternUIState(
            id = newId,
            name = RleParser.getName(rle) ?: name,
            gridSize = RleParser.getGridSize(rle),
            cells = RleParser.decode(rle),
            type = PatternType.CUSTOM // On force le type CUSTOM
        )

        _patterns.update { currentList ->
            (currentList + newPattern).toMutableList()
        }
    }
}
