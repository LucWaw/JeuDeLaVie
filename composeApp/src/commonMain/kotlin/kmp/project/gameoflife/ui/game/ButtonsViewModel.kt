package kmp.project.gameoflife.ui.game

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel


class ButtonsViewModel : ViewModel() {
    private val _isRunning = mutableStateOf(false)
    val isRunning: Boolean
        get() = _isRunning.value


    fun togglePause() {
        _isRunning.value = !_isRunning.value
    }
}

