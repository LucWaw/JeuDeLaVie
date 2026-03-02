package kmp.project.gameoflife.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import kmp.project.gameoflife.domain.modele.PatternType

@Entity
data class Pattern(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val rle: String,
    val type: PatternType = PatternType.CUSTOM
)
