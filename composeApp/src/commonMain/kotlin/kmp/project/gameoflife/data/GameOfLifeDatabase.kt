package kmp.project.gameoflife.data

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import kmp.project.gameoflife.data.dao.PatternDao
import kmp.project.gameoflife.data.entity.Pattern


@Database(
    entities = [
        Pattern::class,
    ],
    version = 1,
    exportSchema = true
)
@ConstructedBy(AppDatabaseConstructor::class)
abstract class GameOfLifeDatabase : RoomDatabase() {

    abstract fun patternDao(): PatternDao

    companion object {
        const val DB_NAME = "game_of_life_db"
    }
}