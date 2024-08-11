package ui

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import data.State
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import model.Space.CellularSpace
import ui.pattern.Pattern

@Composable
fun GameOfLife(
    state: Flow<State>,
    playScope: CoroutineScope,
    onCellClick: (Pair<Int, Int>) -> Unit,
    mutableState: MutableStateFlow<State>,
    space: CellularSpace,
    modifier: Modifier = Modifier
) {
    val stateElement = state.collectAsState(initial = null)

    Column(modifier = modifier) {
        Pattern()


        stateElement.value?.let {
            Board(it, onCellClick)
        }

        Buttons(playScope, space, mutableState)
    }
}