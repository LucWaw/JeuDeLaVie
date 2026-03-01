package kmp.project.gameoflife.ui.pattern

import androidx.lifecycle.ViewModel
import kmp.project.gameoflife.data.repository.service.PatternRepository
import kmp.project.gameoflife.data.service.PatternFakeAPI
import kmp.project.gameoflife.showToast
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
     * Ajoute un nouveau pattern personnalisé.
     * Calcule le "carré minimal" pour normaliser les cellules.
     */
    fun addCustomPattern(cells: List<Pair<Int, Int>>, emptyGridText : String = "", doneText: String = "") {
        if (cells.isEmpty()) {
            showToast(emptyGridText)
            return
        }

        // Calcul du carré minimal (Bounding Box)
        val minX = cells.minOf { it.first }
        val maxX = cells.maxOf { it.first }
        val minY = cells.minOf { it.second }
        val maxY = cells.maxOf { it.second }

        // Normalisation (décalage vers 0,0)
        val normalizedCells = cells.map { (x, y) ->
            Pair(x - minX, y - minY)
        }

        val width = maxX - minX + 1
        val height = maxY - minY + 1
        val gridSize = maxOf(width, height)

        val newId = (_patterns.value.maxOfOrNull { it.id } ?: 0) + 1
        val name = "Custom #$newId"

        val newPattern = PatternUIState(
            id = newId,
            name = name,
            gridSize = gridSize,
            cells = normalizedCells,
            type = PatternType.CUSTOM
        )

        _patterns.update { currentList ->
            (currentList + newPattern).toMutableList()
        }
        
        showToast(doneText)
    }
}
