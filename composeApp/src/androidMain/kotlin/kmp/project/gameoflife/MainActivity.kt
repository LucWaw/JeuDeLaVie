package kmp.project.gameoflife

import App
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            App(isTablet = isTablet())
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}
@Composable
fun isTablet(): Boolean {
    val configuration = LocalConfiguration.current
    return if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
        configuration.screenWidthDp > 840
    } else {
        configuration.screenWidthDp > 600
    }
}