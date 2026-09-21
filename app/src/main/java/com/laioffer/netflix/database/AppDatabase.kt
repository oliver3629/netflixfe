package com.laioffer.netflix.database

import androidx.room.Database
import androidx.room.RoomDatabase

// Main Room database for local app data.
@Database(entities = [VideoEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoriteDao(): FavoriteDao
}