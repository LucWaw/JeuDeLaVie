package kmp.project.gameoflife

sealed class Screens(val route : String = "Game"){
    data object Game: Screens("Game")
    data object Settings: Screens("Settings")
}

