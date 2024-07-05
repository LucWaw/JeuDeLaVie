import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.rickclephas.kmm.viewmodel.KMMViewModel


class GameViewModel : KMMViewModel() {
    private val _isPaused = mutableStateOf(false)
    val isPaused: MutableState<Boolean> = _isPaused


    fun togglePause() {
        _isPaused.value = !_isPaused.value
    }


}

