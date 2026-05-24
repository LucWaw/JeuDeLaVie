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
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
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
import gameoflife.composeapp.generated.resources.page6_en
import gameoflife.composeapp.generated.resources.page6_fr
import kmp.project.gameoflife.data.GameOfLifeDatabase
import kmp.project.gameoflife.di.ToastManager
import kmp.project.gameoflife.ui.onboard.OnboardingUtils
import kmp.project.gameoflife.ui.theme.DarkColorScheme
import kmp.project.gameoflife.ui.theme.LightColorScheme
import org.jetbrains.compose.resources.DrawableResource
import org.koin.dsl.module
import androidx.compose.ui.platform.LocalLocale

private val IS_DYNAMIC_COLOR_SUPPORTED = SDK_INT >= Build.VERSION_CODES.S

class AndroidPlatform : Platform {
    override val name: String = "Android $SDK_INT"
    override val isDynamicColorSupported: Boolean = IS_DYNAMIC_COLOR_SUPPORTED
}

actual fun getPlatform(): Platform = AndroidPlatform()



@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
actual fun GifImage(ressources: DrawableResource, modifier: Modifier) {
    val ressource = when (ressources) {
        Res.drawable.page1 -> R.drawable.page1_presentation_demo
        Res.drawable.page2 -> R.drawable.page2_drag_and_drop
        Res.drawable.page3 -> R.drawable.page3_drawing
        Res.drawable.page4 -> R.drawable.page4_speed
        Res.drawable.page5 -> R.drawable.page5_launch
        Res.drawable.page6_en, Res.drawable.page6_fr -> {
            if (LocalLocale.current.platformLocale.language == "fr") R.drawable.page6_fr_custom_pattern
            else R.drawable.page6_en_custom_pattern
        }
        else -> R.drawable.page1_presentation_demo
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
        R.drawable.page1_presentation_demo -> {
            Image(
                modifier = modifier.width(350.dp).padding(top = 20.dp),
            painter = painter,
            contentDescription = "Presentation of the app"
            )
        }
        R.drawable.page2_drag_and_drop -> {
            Image(
                modifier = modifier.size(width = 350.dp, height = 420.dp).padding(top = 20.dp),
                contentScale = ContentScale.FillHeight,
                painter = painter,
                contentDescription = "You can drag and drops patterns"
            )
        }
        R.drawable.page3_drawing -> {
            Image(
                modifier = modifier.width(350.dp).padding(top = 20.dp),
                painter = painter,
                contentDescription = "You can draw on the grid"
            )
        }
        R.drawable.page4_speed -> {
            Image(
                modifier = modifier.size(width = 350.dp, height = 420.dp).padding(top = 20.dp),
                contentScale = ContentScale.FillHeight,
                painter = painter,
                contentDescription = "You can change the speed of the game"
            )
        }
        R.drawable.page5_launch -> {
            Image(
                modifier = modifier.size(width = 350.dp, height = 420.dp).padding(top = 20.dp),
                contentScale = ContentScale.FillHeight,
                painter = painter,
                contentDescription = "Launch with the bottom right button"
            )
        }
        R.drawable.page6_fr_custom_pattern, R.drawable.page6_en_custom_pattern -> {
            Image(
                modifier = modifier.size(width = 350.dp, height = 420.dp).padding(top = 20.dp),
                contentScale = ContentScale.FillHeight,
                painter = painter,
                contentDescription = "Save your patterns"
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
    val positionInRoot = container.positionInRoot()
    return Offset(event.x - positionInRoot.x, event.y - positionInRoot.y)
}

class AndroidToastManager(private val context: Context) : ToastManager {
    override fun show(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}

fun getDatabaseBuilder(context: Context): RoomDatabase.Builder<GameOfLifeDatabase> {
    val appContext = context.applicationContext
    val dbFile = appContext.getDatabasePath(GameOfLifeDatabase.DB_NAME)
    return Room.databaseBuilder<GameOfLifeDatabase>(
        context = appContext,
        name = dbFile.absolutePath
    )
}


// Dans androidMain/.../Theme.android.kt

@Composable
actual fun platformColors(
    useDarkTheme: Boolean
): ColorScheme {
    return when {
        IS_DYNAMIC_COLOR_SUPPORTED && useDarkTheme -> dynamicDarkColorScheme(LocalContext.current)
        IS_DYNAMIC_COLOR_SUPPORTED && !useDarkTheme -> dynamicLightColorScheme(LocalContext.current)
        useDarkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
}

actual fun platformModule() = module {
    single<DataStore<Preferences>> {
        // Koin inject context with get()
        createDataStore(get())
    }

    single<ToastManager> { AndroidToastManager(get()) }

    single<OnboardingUtils> { AndroidOnboardingUtils(get()) }

    single {
        // Koin inject context with get()
        getDatabaseBuilder(get())
    }
}

fun createDataStore(context: Context): DataStore<Preferences> {
    return PreferenceDataStoreFactory.create(
        produceFile = { context.preferencesDataStoreFile(dataStoreFileName) }
    )
}
