package kmp.project.gameoflife.ui.bottomsheets

import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import kmp.project.gameoflife.ui.draganddrop.DragTargetInfo
import kmp.project.gameoflife.ui.pattern.MovablePatternViewModel
import kmp.project.gameoflife.ui.pattern.Pattern


@Composable
fun BottomSheetsContent(dragTargetInfo: DragTargetInfo) {
    val viewModel = remember { MovablePatternViewModel() }
    val patternsUiState by viewModel.patterns.collectAsState()

    LazyVerticalGrid(columns = GridCells.Adaptive(200.dp)) {

        items(items = patternsUiState) { pattern ->
            Pattern(
                pattern = pattern,
                getPattern = { viewModel.getPatternById(pattern.id) },
                rotatePattern = { viewModel.rotatePattern(pattern.id) },
                dragTargetInfo = dragTargetInfo)
        }
    }
}

