package kmp.project.gameoflife.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import kmp.project.gameoflife.platformColors


val LightColorScheme = lightColorScheme()
val DarkColorScheme = darkColorScheme()

@Composable
fun DragAndDropTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = platformColors(darkTheme),
        content = content,
        shapes = Shapes,
        typography = Typography,
    )
}