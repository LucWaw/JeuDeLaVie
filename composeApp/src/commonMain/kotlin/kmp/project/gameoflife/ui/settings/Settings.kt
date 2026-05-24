package kmp.project.gameoflife.ui.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import gameoflife.composeapp.generated.resources.Res
import gameoflife.composeapp.generated.resources.arrow_back_24px
import gameoflife.composeapp.generated.resources.base_patterns_restored
import gameoflife.composeapp.generated.resources.dynamic_color_info
import gameoflife.composeapp.generated.resources.go_back
import gameoflife.composeapp.generated.resources.grid_columns
import gameoflife.composeapp.generated.resources.grid_rows
import gameoflife.composeapp.generated.resources.grid_size_info
import gameoflife.composeapp.generated.resources.info_24px
import gameoflife.composeapp.generated.resources.restore_base_patterns
import gameoflife.composeapp.generated.resources.restore_base_patterns_info
import gameoflife.composeapp.generated.resources.selectedColor
import gameoflife.composeapp.generated.resources.settings
import gameoflife.composeapp.generated.resources.theme
import gameoflife.composeapp.generated.resources.theme_force_black
import gameoflife.composeapp.generated.resources.theme_force_white
import gameoflife.composeapp.generated.resources.theme_system_classic
import gameoflife.composeapp.generated.resources.theme_system_dynamic
import kmp.project.gameoflife.getPlatform
import kmp.project.gameoflife.ui.theme.ColorTheme
import kmp.project.gameoflife.ui.theme.ThemeViewModel
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Settings(
    isTablet: Boolean,
    goBack: () -> Unit,
    viewModel: ThemeViewModel = koinViewModel()
) {
    val currentTheme by viewModel.themeState.collectAsState()

    val gridRows by viewModel.gridRows.collectAsState()
    val gridColumns by viewModel.gridColumns(isTablet).collectAsState()

    val defaultRows = 20
    val defaultCols = if (isTablet || getPlatform().name.startsWith("Java")) 80 else 20

    var rowsText by remember { mutableStateOf(gridRows.toString()) }
    var isRowsFocused by remember { mutableStateOf(false) }

    var columnsText by remember { mutableStateOf(gridColumns.toString()) }
    var isColsFocused by remember { mutableStateOf(false) }

    LaunchedEffect(gridRows) {
        if (!isRowsFocused) rowsText = gridRows.toString()
    }
    LaunchedEffect(gridColumns) {
        if (!isColsFocused) columnsText = gridColumns.toString()
    }

    Scaffold(
        modifier = Modifier.windowInsetsPadding(WindowInsets.navigationBars),
        topBar = {
            TopAppBar(
                title = { Text(stringResource(Res.string.settings)) },
                navigationIcon = {
                    IconButton(onClick = goBack) {
                        Icon(
                            painter = painterResource(Res.drawable.arrow_back_24px),
                            contentDescription = stringResource(Res.string.go_back)
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        val dynamicThemeLabel = stringResource(Res.string.theme_system_dynamic)
        val systemClassicLabel = stringResource(Res.string.theme_system_classic)
        val forceBlackLabel = stringResource(Res.string.theme_force_black)
        val forceWhiteLabel = stringResource(Res.string.theme_force_white)

        val options = mutableListOf<Pair<String, ColorTheme>>()
        if (getPlatform().isDynamicColorSupported) {
            options.add(dynamicThemeLabel to ColorTheme.DYNAMIC)
        }
        options.add(systemClassicLabel to ColorTheme.SYSTEM)
        options.add(forceBlackLabel to ColorTheme.BLACK)
        options.add(forceWhiteLabel to ColorTheme.WHITE)

        val selectedOption = options.find { it.second == currentTheme } ?: options.first()

        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(stringResource(Res.string.theme),
                    modifier = Modifier.weight(1f))
                var isColorDropdownExpanded by remember {
                    mutableStateOf(false)
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
                        value = selectedOption.first,
                        onValueChange = {},
                        label = { Text(stringResource(Res.string.selectedColor)) },
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
                        options.forEach { (label, theme) ->
                            DropdownMenuItem(
                                text = { Text(label) },
                                onClick = {
                                    viewModel.updateTheme(theme)
                                    isColorDropdownExpanded = false
                                },
                                contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                            )
                        }
                    }
                }
            }

            if (currentTheme == ColorTheme.DYNAMIC) {
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
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = stringResource(Res.string.dynamic_color_info),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Grid Rows Setting
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(stringResource(Res.string.grid_rows), modifier = Modifier.weight(1f))
                OutlinedTextField(
                    value = rowsText,
                    onValueChange = { newValue ->
                        if (newValue.all { it.isDigit() }) {
                            rowsText = newValue
                            val intValue = newValue.toIntOrNull() ?: 0
                            viewModel.updateGridRows(if (intValue in 10..100) intValue else defaultRows)
                        }
                    },
                    modifier = Modifier
                        .width(100.dp)
                        .onFocusChanged { focusState ->
                            isRowsFocused = focusState.isFocused
                            if (!focusState.isFocused) {
                                val intValue = rowsText.toIntOrNull() ?: 0
                                rowsText = if (intValue !in 10..100) defaultRows.toString() else intValue.toString()
                            }
                        },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }

            // Grid Columns Setting
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(stringResource(Res.string.grid_columns), modifier = Modifier.weight(1f))
                OutlinedTextField(
                    value = columnsText,
                    onValueChange = { newValue ->
                        if (newValue.all { it.isDigit() }) {
                            columnsText = newValue
                            val intValue = newValue.toIntOrNull() ?: 0
                            viewModel.updateGridColumns(if (intValue in 10..100) intValue else defaultCols)
                        }
                    },
                    modifier = Modifier
                        .width(100.dp)
                        .onFocusChanged { focusState ->
                            isColsFocused = focusState.isFocused
                            if (!focusState.isFocused) {
                                val intValue = columnsText.toIntOrNull() ?: 0
                                columnsText = if (intValue !in 10..100) defaultCols.toString() else intValue.toString()
                            }
                        },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }

            Row(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    painter = painterResource(Res.drawable.info_24px),
                    contentDescription = "Info grid size",
                    tint = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = stringResource(Res.string.grid_size_info),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 24.dp))

            // Restore Patterns Setting
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = stringResource(Res.string.restore_base_patterns),
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = stringResource(Res.string.restore_base_patterns_info),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                val restoredMessage = stringResource(Res.string.base_patterns_restored)
                OutlinedButton(
                    onClick = { viewModel.restoreBasePatterns(restoredMessage) },
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    Text(stringResource(Res.string.restore_base_patterns))
                }
            }
        }


    }
}
