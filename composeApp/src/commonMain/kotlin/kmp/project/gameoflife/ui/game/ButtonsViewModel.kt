package kmp.project.gameoflife.ui.game

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel


class ButtonsViewModel : ViewModel() {
    private val _isRunning = mutableStateOf(false)
    val isRunning: Boolean
        get() = _isRunning.value

    private val _isEditingMode = mutableStateOf(false)
    val isEditingMode: Boolean
        get() = _isEditingMode.value

    val selectedPatternIds = mutableStateListOf<Int>()


    fun togglePause() {
        _isRunning.value = !_isRunning.value
    }

    fun toggleEditingMode() {
        _isEditingMode.value = !_isEditingMode.value
        if (!_isEditingMode.value) {
            selectedPatternIds.clear()
        }
    }

    fun togglePatternSelection(id: Int) {
        if (selectedPatternIds.contains(id)) {
            selectedPatternIds.remove(id)
        } else {
            selectedPatternIds.add(id)
        }
    }
}
