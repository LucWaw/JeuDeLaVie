package kmp.project.gameoflife

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build.VERSION.SDK_INT
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import coil3.ImageLoader
import coil3.compose.rememberAsyncImagePainter
import coil3.gif.AnimatedImageDecoder
import coil3.gif.GifDecoder
import coil3.request.ImageRequest
import coil3.size.Size
import gameoflife.composeapp.generated.resources.Res
import gameoflife.composeapp.generated.resources.page1
import gameoflife.composeapp.generated.resources.page2
import gameoflife.composeapp.generated.resources.page3
import gameoflife.composeapp.generated.resources.page4
import gameoflife.composeapp.generated.resources.page5
import kmp.project.gameoflife.ui.onboard.OnboardingUtils
import org.jetbrains.compose.resources.DrawableResource


class AndroidPlatform : Platform {
    override val name: String = "Android $SDK_INT"
}

actual fun getPlatform(): Platform = AndroidPlatform()

lateinit var applicationContext: Context

fun initOnboardingUtils(context: Context) {
    applicationContext = context.applicationContext
}

actual fun getOnboardingUtils(): OnboardingUtils {
    return AndroidOnboardingUtils(applicationContext)
}


@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
actual fun GifImage(ressources: DrawableResource, modifier: Modifier) {
    var ressource = when (ressources) {
        Res.drawable.page1 -> R.drawable.page1
        Res.drawable.page2 -> R.drawable.page2
        Res.drawable.page3 -> R.drawable.page3
        Res.drawable.page4 -> R.drawable.page4
        Res.drawable.page5 -> R.drawable.page5
        else -> R.drawable.page1
    }

    val context = LocalContext.current
    val imageLoader = ImageLoader.Builder(context)
        .components {
            if (SDK_INT >= 28) {
                add(AnimatedImageDecoder.Factory())
            } else {
                add(GifDecoder.Factory())
            }
        }
        .build()

    val painter = rememberAsyncImagePainter(
        ImageRequest.Builder(context)
            .data(ressource)
            .size(Size.ORIGINAL)
            .build(),
        imageLoader = imageLoader
    )

    when (ressource) {
        R.drawable.page1 -> {
            Image(
                modifier = Modifier.width(350.dp).padding(top = 20.dp),
            painter = painter,
            contentDescription = "Presentation of the app"
            )

        }
        R.drawable.page2 -> {
            Image(
                modifier = Modifier.size(width = 350.dp, height = 420.dp).padding(top = 20.dp),
                contentScale = ContentScale.FillHeight,
                painter = painter,
                contentDescription = "You can drag and drops patterns"
            )
        }
        R.drawable.page3 -> {
            Image(
                modifier = Modifier.width(350.dp).padding(top = 20.dp),
                painter = painter,
                contentDescription = "You can draw on the grid"
            )
        }
        R.drawable.page4 -> {
            Image(
                modifier = Modifier.size(width = 350.dp, height = 420.dp).padding(top = 20.dp),
                contentScale = ContentScale.FillHeight,
                painter = painter,
                contentDescription = "You can change the speed of the game"
            )
        }
        R.drawable.page5 -> {
            Image(
                modifier = Modifier.size(width = 350.dp, height = 420.dp).padding(top = 20.dp),
                contentScale = ContentScale.FillHeight,
                painter = painter,
                contentDescription = "Launch with the bottom right button"
            )
        }

    }
}


class CroppedShape(
    private val cropStart: Dp = 0.dp,
    private val cropTop: Dp = 0.dp,
    private val cropEnd: Dp = 0.dp,
    private val cropBottom: Dp = 0.dp
) : Shape {

    override fun createOutline(
        size: androidx.compose.ui.geometry.Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        with(density) {
            val left = cropStart.toPx()
            val top = cropTop.toPx()
            val right = size.width - cropEnd.toPx()
            val bottom = size.height - cropBottom.toPx()

            return Outline.Rectangle(Rect(left, top, right, bottom))
        }
    }
}


@Preview
@Composable
fun GifImagePreview() {
    GifImage(ressources = Res.drawable.page1)
}