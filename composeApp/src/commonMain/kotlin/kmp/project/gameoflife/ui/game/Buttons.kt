package kmp.project.gameoflife.ui.game


import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import gameoflife.composeapp.generated.resources.Res
import gameoflife.composeapp.generated.resources.baseline_pause_24
import gameoflife.composeapp.generated.resources.delete_24px
import gameoflife.composeapp.generated.resources.delete_forever_24px
import gameoflife.composeapp.generated.resources.delete_selected_patterns_confirmation
import gameoflife.composeapp.generated.resources.delete_selected_patterns_title
import gameoflife.composeapp.generated.resources.edit_24px
import gameoflife.composeapp.generated.resources.no
import gameoflife.composeapp.generated.resources.play_arrow_24px
import gameoflife.composeapp.generated.resources.yes
import kmp.project.gameoflife.spacing.CellularSpace
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun Buttons(
    playScope: CoroutineScope,
    cellularSpace: CellularSpace,
    updateCells: (List<Pair<Int, Int>>) -> Unit,
    addToCounter: () -> Unit,
    speedValue: StateFlow<Float>,
    capturePreviousGrid: () -> Unit,
    buttonsViewModel: ButtonsViewModel,
    modifier: Modifier = Modifier,
    onDeleteSelectedPatterns: () -> Unit = {}
) {

    var showDeleteConfirmation by remember { mutableStateOf(false) }

    if (showDeleteConfirmation) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirmation = false },
            title = { Text(text = stringResource(Res.string.delete_selected_patterns_title)) },
            text = { Text(text = stringResource(Res.string.delete_selected_patterns_confirmation)) },
            confirmButton = {
                TextButton(
                    onClick = {
                        onDeleteSelectedPatterns()
                        buttonsViewModel.toggleEditingMode()
                        showDeleteConfirmation = false
                    }
                ) {
                    Text(text = stringResource(Res.string.yes))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDeleteConfirmation = false }
                ) {
                    Text(text = stringResource(Res.string.no))
                }
            }
        )
    }

    Row(modifier = modifier.fillMaxWidth()) {
        Button(
            onClick = {
                buttonsViewModel.toggleEditingMode()
            },
            modifier = modifier.weight(0.5f).padding(8.dp)
        ) {
            Icon(
                painter = painterResource(Res.drawable.edit_24px),
                contentDescription = "Edit mode"
            )
        }

        if (buttonsViewModel.isEditingMode) {
            Button(
                onClick = {
                    showDeleteConfirmation = true
                },
                modifier = modifier.weight(0.5f).padding(8.dp)
            ) {
                Icon(
                    painter = painterResource(Res.drawable.delete_forever_24px),
                    contentDescription = "Delete selected"
                )
            }
        } else {
            Button(
                onClick = {
                    //effacer la grille
                    cellularSpace.resetGrid()
                    updateCells(mutableListOf())
                    addToCounter()
                },
                modifier = modifier.weight(0.5f).padding(8.dp)
            ) {
                Icon(
                    painter = painterResource(Res.drawable.delete_24px),
                    contentDescription = "Delete All"
                )
            }
            Button(
                onClick = {
                    buttonsViewModel.togglePause() // Met le jeu en pause ou en marche

                    if (buttonsViewModel.isRunning) {
                        capturePreviousGrid() // On enregistre l'état actuel comme "précédent" avant de lancer la boucle
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
                modifier = modifier.weight(0.5f).padding(8.dp)
            ) {
                val painter = painterResource(Res.drawable.baseline_pause_24)
                if (buttonsViewModel.isRunning) {
                    Icon(painter, contentDescription = "pause")
                } else {
                    Icon(
                        painter = painterResource(Res.drawable.play_arrow_24px),
                        contentDescription = "Play"
                    )
                }
            }
        }









    }
}
