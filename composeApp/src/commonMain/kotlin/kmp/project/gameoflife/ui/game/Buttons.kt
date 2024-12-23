package kmp.project.gameoflife.ui.game


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
import gameoflife.composeapp.generated.resources.Res
import gameoflife.composeapp.generated.resources.baseline_pause_24
import kmp.project.gameoflife.spacing.CellularSpace
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow
import org.jetbrains.compose.resources.painterResource

@Composable
fun Buttons(
    playScope: CoroutineScope,
    cellularSpace: CellularSpace,
    updateCells: (List<Pair<Int, Int>>) -> Unit,
    addToCounter: () -> Unit,
    speedValue: StateFlow<Float>,
    modifier: Modifier = Modifier
) {
    //play button with play icon
    val buttonsViewModel = remember { ButtonsViewModel() }

    Row(modifier = modifier.fillMaxWidth()) {
        Button(
            onClick = {
                //effacer la grille
                cellularSpace.resetGrid()
                updateCells(mutableListOf())
                addToCounter()
            },
            modifier = modifier.weight(0.5f).padding(16.dp)
        ) {
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
                        { updateCells(it) },
                        addToCounter,
                        cellularSpace,
                        buttonsViewModel,
                        speedValue
                    )
                }
            },
            modifier = modifier.weight(0.5f).padding(16.dp)
        ) {
            val painter = painterResource(Res.drawable.baseline_pause_24)
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
