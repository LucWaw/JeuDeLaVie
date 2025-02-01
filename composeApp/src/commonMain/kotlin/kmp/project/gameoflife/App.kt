package kmp.project.gameoflife

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kmp.project.gameoflife.ui.GameOfLife
import kmp.project.gameoflife.ui.bottomsheets.BottomSheetsContent
import kmp.project.gameoflife.ui.draganddrop.DragAndDropTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App(isTablet: Boolean = false) {
    DragAndDropTheme {
        BottomSheetScaffold(
            sheetContent = {
                BottomSheetsContent()
            },
            sheetShape = MaterialTheme.shapes.large,
            sheetBackgroundColor = MaterialTheme.colors.surface,
            sheetElevation = 16.dp,
            modifier = Modifier.fillMaxSize()
        ) {
            GameOfLife(isTablet)
        }

    }
}