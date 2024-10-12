package ui.ViewModels

import androidx.compose.runtime.mutableStateOf
import com.rickclephas.kmm.viewmodel.KMMViewModel


class GameViewModel : KMMViewModel() {
    private val _isRunning = mutableStateOf(false)
    val isRunning: Boolean
        get() = _isRunning.value


    fun togglePause() {
        _isRunning.value = !_isRunning.value
    }
}

