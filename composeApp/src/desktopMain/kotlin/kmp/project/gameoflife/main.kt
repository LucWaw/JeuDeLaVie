package kmp.project.gameoflife

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "GameOfLife",
    ) {
        window.minimumSize = java.awt.Dimension(400,1000)

        App()
    }
}