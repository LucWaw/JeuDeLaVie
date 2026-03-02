package kmp.project.gameoflife

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import kmp.project.gameoflife.di.initKoin

fun main() {
    initKoin()
    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "GameOfLife",
        ) {
            window.minimumSize = java.awt.Dimension(400, 1000)

            App()
        }
    }
}
