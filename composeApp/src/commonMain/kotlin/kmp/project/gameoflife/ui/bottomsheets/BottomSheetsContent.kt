package kmp.project.gameoflife.ui.bottomsheets

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kmp.project.gameoflife.data.repository.service.PatternRepository
import kmp.project.gameoflife.data.service.PatternFakeAPI
import kmp.project.gameoflife.ui.pattern.favoriteButton


@Composable
fun BottomSheetsContent() {
    val listOfPatterns = PatternRepository(PatternFakeAPI()).getAllPatterns()
    val isInDark = isSystemInDarkTheme()

    LazyVerticalGrid(columns = GridCells.Adaptive(200.dp)) {
        items(listOfPatterns) { pattern ->
            Box {
                LazyVerticalGrid(
                    GridCells.Fixed(pattern.gridSize),
                    modifier = Modifier.aspectRatio(1f)
                ) {
                    items(pattern.gridSize * pattern.gridSize) {
                        val cellCoord = Pair(it / pattern.gridSize, it % pattern.gridSize)
                        Box(
                            modifier = Modifier
                                .aspectRatio(1f)
                                .background(
                                    if (pattern.cells.contains(cellCoord)) {
                                        if (isInDark) {
                                            Color.White
                                        } else {
                                            Color.Black
                                        }
                                    } else Color.Transparent
                                )
                                .border(
                                    if (pattern.cells.contains(cellCoord)) {
                                        BorderStroke(1.dp, Color.DarkGray)
                                    } else BorderStroke(0.dp, Color.LightGray)
                                )
                        )
                    }
                }
                Box(modifier = Modifier.background(MaterialTheme.colors.primary).align(Alignment.TopEnd)) {
                    favoriteButton()
                }
            }

        }
    }
}

