package ui.game


import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import model.spacing.CellularSpace
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalResourceApi::class)
@Composable
fun Buttons(
    playScope: CoroutineScope,
    cellularSpace: CellularSpace,
    mutableGameUIState: (List<Pair<Int, Int>>) -> Unit,
    modifier: Modifier = Modifier
) {
    //play button with play icon
    val buttonsViewModel = remember { ButtonsViewModel() }

    Row(modifier = modifier.fillMaxWidth().padding(16.dp)){
        Button(
            onClick = {
                //effacer la grille
                cellularSpace.resetGrid()
                mutableGameUIState(mutableListOf())
            },
            modifier = modifier.weight(0.5f)){
            Icon(
                imageVector = Icons.Filled.Delete,
                contentDescription = "Delete All"
            )
        }
        Button(
            onClick = {
                buttonsViewModel.togglePause() // Met le jeu en pause ou en marche

                if (buttonsViewModel.isRunning) {
                    runGameLoop(
                        playScope,
                        { mutableGameUIState(it) },
                        cellularSpace,
                        buttonsViewModel
                    )
                }
            },
            modifier = modifier.weight(0.5f)
        ) {
            val painter = painterResource("baseline_pause_24.xml")
            if (buttonsViewModel.isRunning) {
                Icon(painter, contentDescription = "pause")
            } else {
                Icon(
                    imageVector = Icons.Filled.PlayArrow,
                    contentDescription = "Play"
                )
            }
        }

    }
}
