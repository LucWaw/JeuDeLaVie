package kmp.project.gameoflife

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.unit.dp
import kmp.project.gameoflife.ui.GameOfLife
import kmp.project.gameoflife.ui.bottomsheets.BottomSheetsContent
import kmp.project.gameoflife.ui.draganddrop.DragAndDropTheme
import kmp.project.gameoflife.ui.draganddrop.DragTargetInfo
import kmp.project.gameoflife.ui.draganddrop.LongPressDraggable
import kmp.project.gameoflife.ui.isDesktop
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App(isTablet: Boolean = false) {
    DragAndDropTheme {
        //placeholder


        //With remeber
        val _gridSize = remember { MutableStateFlow(Size.Zero) }
        val gridSize: StateFlow<Size> = _gridSize.asStateFlow()
        fun modifyGridSize(gridSize: Size) {
            _gridSize.value = gridSize
        }
        val dragTargetInfo = remember { DragTargetInfo() }

        LongPressDraggable(
            modifier = Modifier.width(2000.dp),
            gridSize,
            if (isDesktop || isTablet) 80 else 15,
            dragTargetInfo
        ) {
            BottomSheetScaffold( //Not modal because ModalBottomSheetLayout creates a shadow above the rest of the screen especially in dark mode
                sheetContent = {
                    BottomSheetsContent(dragTargetInfo)
                },
                sheetShape = MaterialTheme.shapes.large,
                sheetBackgroundColor = MaterialTheme.colors.surface,
                sheetElevation = 16.dp,
                modifier = Modifier.fillMaxSize()
            ) {
                GameOfLife(isTablet, { modifyGridSize(it) }, gridSize, dragTargetInfo)
            }
        }

    }
}