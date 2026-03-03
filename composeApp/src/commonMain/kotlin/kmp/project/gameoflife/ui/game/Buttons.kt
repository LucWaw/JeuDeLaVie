package kmp.project.gameoflife.ui.game


import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
            onDismissRequest = { showDeleteConfirmation = false },//Never read but useful for remember
            title = { Text(text = stringResource(Res.string.delete_selected_patterns_title)) },
            text = { Text(text = stringResource(Res.string.delete_selected_patterns_confirmation)) },
            confirmButton = {
                TextButton(
                    onClick = {
                        onDeleteSelectedPatterns()
                        onToggleEditingMode()
                        showDeleteConfirmation = false//Never read but useful for remember
                    },
                    colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)
                ) {
                    Text(text = stringResource(Res.string.yes))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDeleteConfirmation = false }//Never read but useful for remember
                ) {
                    Text(text = stringResource(Res.string.no))
                }
            }
        )
    }

    Row(modifier = modifier.fillMaxWidth().padding(horizontal = 8.dp)) {
        FilledTonalButton(
            onClick = {
                onToggleEditingMode()
            },
            modifier = Modifier.weight(1f).padding(4.dp),
            colors = ButtonDefaults.filledTonalButtonColors(
                containerColor = if (isEditingMode) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.surfaceVariant,
                contentColor = if (isEditingMode) MaterialTheme.colorScheme.onSecondaryContainer else MaterialTheme.colorScheme.onSurfaceVariant
            )
        ) {
            Icon(
                painter = painterResource(Res.drawable.edit_24px),
                contentDescription = "Edit mode"
            )
        }

        if (isEditingMode) {
            Button(
                onClick = {
                    showDeleteConfirmation = true //Never read but useful for remember
                },
                modifier = Modifier.weight(1f).padding(4.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error,
                    contentColor = MaterialTheme.colorScheme.onError
                )
            ) {
                Icon(
                    painter = painterResource(Res.drawable.delete_forever_24px),
                    contentDescription = "Delete selected"
                )
            }
        } else {
            FilledTonalButton(
                onClick = {
                    cellularSpace.resetGrid()
                    updateCells(mutableListOf())
                    addToCounter()
                },
                modifier = Modifier.weight(1f).padding(4.dp),
                colors = ButtonDefaults.filledTonalButtonColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer,
                    contentColor = MaterialTheme.colorScheme.onErrorContainer
                )
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
                modifier = Modifier.weight(1f).padding(4.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isRunning) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.primary
                )
            ) {
                val painter = if (isRunning) 
                    painterResource(Res.drawable.baseline_pause_24) 
                else 
                    painterResource(Res.drawable.play_arrow_24px)
                
                Icon(painter, contentDescription = if (isRunning) "pause" else "Play")
            }
        }
    }
}