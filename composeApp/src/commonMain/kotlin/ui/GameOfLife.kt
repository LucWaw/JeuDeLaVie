package ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import model.State
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import model.Space.CellularSpace
import ui.pattern.PatternsUI

@Composable
fun GameOfLife(
    playScope: CoroutineScope,
    onCellClick: (Pair<Int, Int>) -> Unit,
    mutableState: MutableStateFlow<State>,
    space: CellularSpace,
    modifier: Modifier = Modifier
) {
    Column {
        LongPressDraggable(modifier = modifier.width(1000.dp)) {
            Column {

                //Game board
                mutableState.collectAsState().value.let { //ou if stateElement.value != null
                    Board(it, onCellClick, modifier)
                }

                PatternsUI()
            }
        }
        //Play pause button
        Buttons(playScope, space, mutableState, modifier)
    }
}