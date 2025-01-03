
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ui.GameOfLife
import ui.draganddrop.DragAndDropTheme


@Composable
fun App(isTablet : Boolean = false) {
    DragAndDropTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {
            GameOfLife(isTablet)
        }
    }
}


