package com.lucwaw.androidapp

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import kmp.project.gameoflife.App
import kmp.project.gameoflife.initOnboardingUtils

class MainActivity : ComponentActivity() {
    @SuppressLint("SourceLockedOrientationActivity")
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initOnboardingUtils(this)

        setContent {
            val windowSizeClass = calculateWindowSizeClass(this)
            if (windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact ||
                windowSizeClass.heightSizeClass == WindowHeightSizeClass.Compact

            ) {
                requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            }
            App(isTablet = windowSizeClass.widthSizeClass == WindowWidthSizeClass.Expanded)
        }

    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun Preview(){
    /*
    Scaffold(
        modifier = Modifier.windowInsetsPadding(WindowInsets.navigationBars),
        topBar = {
            TopAppBar({ Text("string Settings") })
        }
    ) { paddingValues ->
        Row(modifier = Modifier.padding(paddingValues)) {
            Column {
                Text("string Theme")
                var isAisleDropdownExpanded by remember {
                    mutableStateOf(
                        false
                    )
                }



                data class DeleteRadioOptions(
                    val name: String,
                    val method: () -> Unit
                )

                var selectedAisle by remember { mutableStateOf(aisleOptions[0]) }
                val deleteRadioOptions = DeleteRadioOptions(movingAll) {
                    onConfirmDeleteByMovingAllMedicine(selectedAisle)
                }
                val (selectedOption, onOptionSelected) = remember {
                    mutableStateOf(
                        deleteRadioOptions[0].name
                    )
                }

                ExposedDropdownMenuBox(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(),
                    expanded = isAisleDropdownExpanded,
                    onExpandedChange = {
                        isAisleDropdownExpanded = !isAisleDropdownExpanded
                    },
                ) {
                    OutlinedTextField(
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth(),
                        readOnly = true,
                        value = selectedAisle.ifEmpty { stringResource(R.string.select_aisle_placeholder) },
                        onValueChange = {},
                        label = { Text(stringResource(R.string.aisle_label)) },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(
                                expanded = isAisleDropdownExpanded
                            )
                        },
                        colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors()
                    )

                    ExposedDropdownMenu(
                        modifier = Modifier.fillMaxWidth(),
                        expanded = isAisleDropdownExpanded,
                        onDismissRequest = {
                            isAisleDropdownExpanded = false
                        },
                    ) {
                        aisleOptions.forEach { selectionOption ->
                            DropdownMenuItem(
                                modifier = Modifier.fillMaxWidth(),
                                text = { Text(selectionOption) },
                                onClick = {
                                    selectedAisle = selectionOption
                                    isAisleDropdownExpanded = false
                                },
                                contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                            )
                        }

                    }
                }
            }
        }
    }*/
}