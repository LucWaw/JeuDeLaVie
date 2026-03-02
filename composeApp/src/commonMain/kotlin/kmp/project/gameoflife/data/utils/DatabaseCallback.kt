package kmp.project.gameoflife.data.utils

import androidx.room.RoomDatabase
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.execSQL

object DatabaseCallback : RoomDatabase.Callback() {
    override fun onCreate(connection: SQLiteConnection) {
        super.onCreate(connection)
        prepopulate(connection)
    }

    private fun prepopulate(connection: SQLiteConnection) {
        InitialData.patterns.forEach { pattern ->
            connection.execSQL(
                "INSERT INTO Pattern (id, rle, type) VALUES (${pattern.id}, '${pattern.rle}', '${pattern.type.name}')"
            )
        }
    }
}
