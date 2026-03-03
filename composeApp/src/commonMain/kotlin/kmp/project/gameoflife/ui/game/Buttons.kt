package kmp.project.gameoflife.ui.game


import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun Buttons(
    cellularSpace: CellularSpace,
    updateCells: (List<Pair<Int, Int>>) -> Unit,
    addToCounter: () -> Unit,
    isEditingMode: Boolean,
    isRunning: Boolean,
    onToggleEditingMode: () -> Unit,
    onTogglePause: () -> Unit,
    modifier: Modifier = Modifier,
    onDeleteSelectedPatterns: () -> Unit = {}
) {

    var showDeleteConfirmation by remember { mutableStateOf(false) }

    if (showDeleteConfirmation) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirmation = false },//Not really never read
            title = { Text(text = stringResource(Res.string.delete_selected_patterns_title)) },
            text = { Text(text = stringResource(Res.string.delete_selected_patterns_confirmation)) },
            confirmButton = {
                TextButton(
                    onClick = {
                        onDeleteSelectedPatterns()
                        onToggleEditingMode()
                        showDeleteConfirmation = false //Not really never read
                    }
                ) {
                    Text(text = stringResource(Res.string.yes))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDeleteConfirmation = false }//Not really never read
                ) {
                    Text(text = stringResource(Res.string.no))
                }
            }
        )
    }

    Row(modifier = modifier.fillMaxWidth()) {
        Button(
            onClick = {
                onToggleEditingMode()
            },
            modifier = modifier.weight(0.5f).padding(8.dp)
        ) {
            Icon(
                painter = painterResource(Res.drawable.edit_24px),
                contentDescription = "Edit mode"
            )
        }

        if (isEditingMode) {
            Button(
                onClick = {
                    showDeleteConfirmation = true //Not really never read
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
                    onTogglePause()
                },
                modifier = modifier.weight(0.5f).padding(8.dp)
            ) {
                val painter = painterResource(Res.drawable.baseline_pause_24)
                if (isRunning) {
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
