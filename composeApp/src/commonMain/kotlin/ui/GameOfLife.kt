package ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.MutableStateFlow
import model.Space.CellularSpace
import ui.draganddrop.LongPressDraggable
import ui.game.Board
import ui.game.Buttons
import ui.pattern.PatternsUI

@Composable
fun GameOfLife(
    onCellClick: (Pair<Int, Int>) -> Unit,
    mutableGameUIState: MutableStateFlow<GameUIState>,
    space: CellularSpace,
    modifier: Modifier = Modifier
) {


    Column {
        LongPressDraggable(modifier = modifier.width(1000.dp)) {
            Column {

                //Game board
                mutableGameUIState.collectAsState().value.let { //ou if stateElement.value != null
                    Board(it, onCellClick, modifier)
                }

                PatternsUI()
            }
        }
        //Play pause button
        val playScope = rememberCoroutineScope()
        Buttons(playScope, space, mutableGameUIState, modifier)
    }
}