package kmp.project.gameoflife

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import kmp.project.gameoflife.ui.draganddrop.Purple500

class MainActivity : ComponentActivity() {
    @SuppressLint("SourceLockedOrientationActivity")
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initOnboardingUtils(this)



        setContent {
            val windowSizeClass = calculateWindowSizeClass(this)
            if (windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact ||
                windowSizeClass.heightSizeClass == WindowHeightSizeClass.Compact

                ){
                requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                }
            App(isTablet = windowSizeClass.widthSizeClass == WindowWidthSizeClass.Expanded)
        }

    }
}

@Composable
fun AppAndroidPreview() {
    App()
}




@Composable
@Preview
fun ComposeLikeBox(modifier : Modifier = Modifier){
    Box(modifier = modifier.background(Purple500)) {
        IconButton(
            onClick = {},
            modifier = Modifier
                .align(Alignment.TopEnd)
        ) {
            Icon(
                imageVector = Icons.Rounded.Favorite,
                contentDescription = "Put in favorite",
                tint = Color.Yellow
            )
        }
    }
}