package kmp.project.gameoflife.ui.pattern

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kmp.project.gameoflife.domain.modele.PatternType
import kmp.project.gameoflife.domain.modele.PatternMovable
import kmp.project.gameoflife.domain.usecase.AddAPattern
import kmp.project.gameoflife.domain.usecase.DeletePattern
import kmp.project.gameoflife.domain.usecase.GetAllPatterns
import kmp.project.gameoflife.showToast
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class MovablePatternViewModel(
    getAllPatterns: GetAllPatterns,
    private val addPattern: AddAPattern,
    private val deletePattern: DeletePattern
): ViewModel()  {

    // Stocke les patterns modifiés (rotation) uniquement en mémoire
    private val _rotatedPatterns = MutableStateFlow<Map<Long, PatternMovable>>(emptyMap())

    // Combine les patterns de Room avec les rotations en mémoire
    val patterns: StateFlow<List<PatternMovable>> = getAllPatterns()
        .combine(_rotatedPatterns) { roomPatterns, rotatedMap ->
            roomPatterns.map { roomPattern ->
                rotatedMap[roomPattern.id] ?: roomPattern
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun getPatternById(id: Long): PatternMovable? {
        return patterns.value.find { it.id == id }
    }

    /**
     * Tourner les cellules du pattern de 90 degrés dans le sens horaire.
     * La rotation reste uniquement en mémoire.
     */
    fun rotatePattern(id : Long) {
        val currentPattern = getPatternById(id) ?: return
        val newCells = currentPattern.cells.map { (y, x) ->
            Pair(x, currentPattern.gridSize - 1 - y)
        }
        _rotatedPatterns.update { it + (id to currentPattern.copy(cells = newCells)) }
    }

    /**
     * Ajoute un nouveau pattern personnalisé.
     * L'ajout est persisté via Room.
     */
    fun addCustomPattern(cells: Set<Pair<Int, Int>>, text: String = "") {
        if (cells.isEmpty()) {
            showToast(text)
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

        val nextIdSuffix = (patterns.value.maxOfOrNull { it.id } ?: 0) + 1
        val name = "Custom #$nextIdSuffix"

        val newPattern = PatternMovable(
            name = name,
            gridSize = gridSize,
            cells = normalizedCells,
            type = PatternType.CUSTOM
        )

        viewModelScope.launch {
            addPattern.invoke(newPattern)
            showToast(text)
        }
    }

    /**
     * Supprime les patterns spécifiés.
     * La suppression est gérée par Room.
     */
    fun deletePatterns(ids: List<Long>) {
        viewModelScope.launch {
            // Nettoyage de la mémoire
            _rotatedPatterns.update { it - ids.toSet() }
            
            val patternsToDelete = patterns.value.filter { it.id in ids }
            patternsToDelete.forEach { 
                deletePattern.invoke(it)
            }
        }
    }
}
