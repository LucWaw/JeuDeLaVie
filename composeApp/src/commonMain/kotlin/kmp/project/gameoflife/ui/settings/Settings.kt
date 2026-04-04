package kmp.project.gameoflife.ui.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import gameoflife.composeapp.generated.resources.Res
import gameoflife.composeapp.generated.resources.arrow_back_24px
import gameoflife.composeapp.generated.resources.dynamic_color_info
import gameoflife.composeapp.generated.resources.go_back
import gameoflife.composeapp.generated.resources.info_24px
import gameoflife.composeapp.generated.resources.selectedColor
import gameoflife.composeapp.generated.resources.settings
import gameoflife.composeapp.generated.resources.theme
import gameoflife.composeapp.generated.resources.theme_force_black
import gameoflife.composeapp.generated.resources.theme_force_white
import gameoflife.composeapp.generated.resources.theme_system_classic
import gameoflife.composeapp.generated.resources.theme_system_dynamic
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Settings(goBack: () -> Boolean) {
    Scaffold(
        modifier = Modifier.windowInsetsPadding(WindowInsets.navigationBars),
        topBar = {
            TopAppBar(
                title = { Text(stringResource(Res.string.settings)) },
                navigationIcon = {
                    IconButton(onClick = {
                        goBack()
                    }) {
                        Icon(
                            painter = painterResource(Res.drawable.arrow_back_24px),
                            contentDescription = stringResource(Res.string.go_back)
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        val colorOptions =
            listOf(stringResource(Res.string.theme_system_dynamic),stringResource(Res.string.theme_system_classic), stringResource(Res.string.theme_force_black), stringResource(Res.string.theme_force_white))


        var selectedColor by remember { mutableStateOf(colorOptions[0]) }

        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(stringResource(Res.string.theme))
                var isColorDropdownExpanded by remember {
                    mutableStateOf(
                        false
                    )
                }


                ExposedDropdownMenuBox(

                    expanded = isColorDropdownExpanded,
                    onExpandedChange = {
                        isColorDropdownExpanded = !isColorDropdownExpanded
                    },
                ) {
                    OutlinedTextField(
                        modifier = Modifier
                            .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryEditable, true),
                        readOnly = true,
                        value = selectedColor,
                        onValueChange = {},
                        label = { stringResource(Res.string.selectedColor) },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(
                                expanded = isColorDropdownExpanded
                            )
                        },
                        colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors()
                    )

                    ExposedDropdownMenu(

                        expanded = isColorDropdownExpanded,
                        onDismissRequest = {
                            isColorDropdownExpanded = false
                        },
                    ) {
                        colorOptions.forEach { selectionOption ->
                            DropdownMenuItem(
                                text = { Text(selectionOption) },
                                onClick = {
                                    selectedColor = selectionOption
                                    //TODO ACTUALY CHANGE THEME
                                    isColorDropdownExpanded = false
                                },
                                contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                            )
                        }

                    }
                }
            }
            if (selectedColor == colorOptions[0]) {
                Row(
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.info_24px),
                        contentDescription = "Info dynamic color",
                        tint = androidx.compose.material3.MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = stringResource(Res.string.dynamic_color_info),
                        style = androidx.compose.material3.MaterialTheme.typography.bodySmall,
                        color = androidx.compose.material3.MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}
