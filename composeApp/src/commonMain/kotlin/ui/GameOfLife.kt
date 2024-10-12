package ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import data.State
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import model.Space.CellularSpace
import ui.pattern.MovablePatternViewModel
import ui.pattern.PatternUI
import ui.pattern.patternList

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

                LazyRow(
                    modifier = modifier.height(250.dp),
                ) {
                    items(items = patternList) { pattern ->
                        val  viewModel = MovablePatternViewModel()
                        viewModel.init(pattern)
                        PatternUI(viewModel)
                    }
                }
            }
        }
        //Play pause button
        Buttons(playScope, space, mutableState, modifier)
    }
}