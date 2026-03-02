package kmp.project.gameoflife.data

import androidx.room.RoomDatabaseConstructor

@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object AppDatabaseConstructor :
    RoomDatabaseConstructor<GameOfLifeDatabase>