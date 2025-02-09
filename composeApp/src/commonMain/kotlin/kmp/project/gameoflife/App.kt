package kmp.project.gameoflife

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import kmp.project.gameoflife.ui.GameOfLife
import kmp.project.gameoflife.ui.draganddrop.DragAndDropTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App(isTablet: Boolean = false) {
    DragAndDropTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {
            GameOfLife(isTablet)
        }

    }
}