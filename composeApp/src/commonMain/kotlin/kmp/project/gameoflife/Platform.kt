package kmp.project.gameoflife

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform