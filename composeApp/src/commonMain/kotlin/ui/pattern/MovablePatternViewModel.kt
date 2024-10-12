package ui.pattern

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.rickclephas.kmm.viewmodel.KMMViewModel


class MovablePatternViewModel: KMMViewModel()  {
    private lateinit var patternState : MutableState<Pattern>

    fun init(pattern: Pattern) {
        patternState = mutableStateOf(pattern)
    }

    fun getPattern(): MutableState<Pattern> {
        return patternState
    }

    fun changePattern(pattern: Pattern) {
        patternState.value = pattern
    }


}