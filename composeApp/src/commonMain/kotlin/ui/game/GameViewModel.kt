package ui.game

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel


class GameViewModel : ViewModel() {
    private val _isRunning = mutableStateOf(false)
    val isRunning: Boolean
        get() = _isRunning.value


    fun togglePause() {
        _isRunning.value = !_isRunning.value
    }
}

