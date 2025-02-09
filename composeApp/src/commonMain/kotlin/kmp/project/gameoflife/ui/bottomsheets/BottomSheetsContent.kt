package kmp.project.gameoflife.ui.bottomsheets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kmp.project.gameoflife.ui.draganddrop.DragTargetInfo
import kmp.project.gameoflife.ui.pattern.MovablePatternViewModel
import kmp.project.gameoflife.ui.pattern.Pattern


@Composable
fun BottomSheetsContent(dragTargetInfo: DragTargetInfo, lazyGridState: LazyGridState) {
    val viewModel = remember { MovablePatternViewModel() }
    val patternsUiState by viewModel.patterns.collectAsState()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.heightIn(min = 100.dp, max = 800.dp)
    ) {

        Spacer(//Using this to create a kind of Icon that tells the user that the sheet is expandable
            modifier = Modifier
                .wrapContentSize()
                .height(3.dp)
                .width(70.dp)
                .background(Color.White)
        )

        LazyVerticalGrid(columns = GridCells.Adaptive(200.dp), state = lazyGridState) {

            items(items = patternsUiState) { pattern ->
                Pattern(
                    pattern = pattern,
                    getPattern = { viewModel.getPatternById(pattern.id) },
                    rotatePattern = { viewModel.rotatePattern(pattern.id) },
                    dragTargetInfo = dragTargetInfo,
                    width = 2000
                )
            }
        }
    }
}

