import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.rickclephas.kmm.viewmodel.KMMViewModel


class GameViewModel : KMMViewModel() {
    private val _isRunning = mutableStateOf(false)
    val isRunning: MutableState<Boolean> = _isRunning


    fun togglePause() {
        _isRunning.value = !_isRunning.value
    }
}

