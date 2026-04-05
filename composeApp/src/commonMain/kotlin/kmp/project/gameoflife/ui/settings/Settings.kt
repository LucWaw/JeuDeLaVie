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
import androidx.compose.runtime.collectAsState
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
import kmp.project.gameoflife.getPlatform
import kmp.project.gameoflife.ui.theme.ColorTheme
import kmp.project.gameoflife.ui.theme.ThemeViewModel
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Settings(
    goBack: () -> Boolean,
    viewModel: ThemeViewModel = koinViewModel()
) {
    val currentTheme by viewModel.themeState.collectAsState()

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
