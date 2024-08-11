package ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
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

    Column(modifier = modifier.fillMaxSize()) {
        //Pattern()

        LongPressDraggable (modifier = modifier){

            //Column(modifier = modifier) {
                LazyRow (
                    modifier = Modifier,
                    contentPadding = PaddingValues(horizontal = 10.dp)
                ) {
                    items(items = patternList) { pattern ->
                        PatternUI(pattern)
                    }
                }

                mutableState.collectAsState().value.let { //ou if stateElement.value != null
                    println("here")
                    Board(it, onCellClick)
                }
            //}


        }



        Buttons(playScope, space, mutableState)
    }
}