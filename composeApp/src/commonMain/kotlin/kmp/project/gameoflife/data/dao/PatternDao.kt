package kmp.project.gameoflife.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kmp.project.gameoflife.data.entity.Pattern
import kotlinx.coroutines.flow.Flow


@Dao
interface PatternDao {

    @Query("SELECT * FROM Pattern")
    fun getAllPatterns(): Flow<List<Pattern>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(pattern: Pattern)

    @Delete
    suspend fun delete(pattern: Pattern)
}
