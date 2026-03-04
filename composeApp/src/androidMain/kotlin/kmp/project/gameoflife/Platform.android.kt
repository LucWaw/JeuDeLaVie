package kmp.project.gameoflife

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipDescription
import android.content.Context
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draganddrop.DragAndDropEvent
import androidx.compose.ui.draganddrop.DragAndDropTransferData
import androidx.compose.ui.draganddrop.mimeTypes
import androidx.compose.ui.draganddrop.toAndroidDragEvent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.room.Room
import androidx.room.RoomDatabase
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
import kmp.project.gameoflife.data.GameOfLifeDatabase
import kmp.project.gameoflife.ui.onboard.OnboardingUtils
import kmp.project.gameoflife.ui.theme.DarkColorScheme
import kmp.project.gameoflife.ui.theme.LightColorScheme
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
    val ressource = when (ressources) {
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
                modifier = modifier.width(350.dp).padding(top = 20.dp),
            painter = painter,
            contentDescription = "Presentation of the app"
            )

        }
        R.drawable.page2 -> {
            Image(
                modifier = modifier.size(width = 350.dp, height = 420.dp).padding(top = 20.dp),
                contentScale = ContentScale.FillHeight,
                painter = painter,
                contentDescription = "You can drag and drops patterns"
            )
        }
        R.drawable.page3 -> {
            Image(
                modifier = modifier.width(350.dp).padding(top = 20.dp),
                painter = painter,
                contentDescription = "You can draw on the grid"
            )
        }
        R.drawable.page4 -> {
            Image(
                modifier = modifier.size(width = 350.dp, height = 420.dp).padding(top = 20.dp),
                contentScale = ContentScale.FillHeight,
                painter = painter,
                contentDescription = "You can change the speed of the game"
            )
        }
        R.drawable.page5 -> {
            Image(
                modifier = modifier.size(width = 350.dp, height = 420.dp).padding(top = 20.dp),
                contentScale = ContentScale.FillHeight,
                painter = painter,
                contentDescription = "Launch with the bottom right button"
            )
        }

    }
}


@Preview
@Composable
fun GifImagePreview() {
    GifImage(ressources = Res.drawable.page1)
}


// Build data using Android's ClipData
actual fun buildTextTransferData(text: String, dragOffset: Offset): DragAndDropTransferData {
    return DragAndDropTransferData(
        clipData = ClipData.newPlainText("Dragged Text", text)
    )
}

// Check mime types using Android's DragEvent bindings
actual fun DragAndDropEvent.hasText(): Boolean {
    return this.mimeTypes().contains(ClipDescription.MIMETYPE_TEXT_PLAIN)
}

// Extract the text safely
actual fun DragAndDropEvent.getText(): String? {
    val clipData = this.toAndroidDragEvent().clipData ?: return null
    if (clipData.itemCount > 0) {
        return clipData.getItemAt(0).text?.toString()
    }
    return null
}

actual fun DragAndDropEvent.getPositionIn(container: LayoutCoordinates): Offset {
    val event = this.toAndroidDragEvent()
    return Offset(event.x, event.y)
}

actual fun showToast(message: String) {
    Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
}

fun getDatabaseBuilder(context: Context): RoomDatabase.Builder<GameOfLifeDatabase> {
    val appContext = context.applicationContext
    val dbFile = appContext.getDatabasePath(GameOfLifeDatabase.DB_NAME)
    return Room.databaseBuilder<GameOfLifeDatabase>(
        context = appContext,
        name = dbFile.absolutePath
    )
}

actual fun getDatabaseBuilder(): RoomDatabase.Builder<GameOfLifeDatabase> {
    return getDatabaseBuilder(applicationContext)
}


// Dans androidMain/.../Theme.android.kt

@Composable
actual fun platformColors(
    useDarkTheme: Boolean
): ColorScheme {
    val dynamicColor = SDK_INT >= Build.VERSION_CODES.S
    return when {
        dynamicColor && useDarkTheme -> dynamicDarkColorScheme(LocalContext.current)
        dynamicColor && !useDarkTheme -> dynamicLightColorScheme(LocalContext.current)
        useDarkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
}
