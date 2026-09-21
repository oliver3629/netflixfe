package com.laioffer.netflix.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

// Room DAO for reading and writing locally saved favorite videos.
@Dao
interface FavoriteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: VideoEntity)

    @Query("DELETE FROM favorites WHERE id = :videoId")
    suspend fun deleteByVideoId(videoId: String)

    @Query("SELECT EXISTS(SELECT 1 FROM favorites WHERE id = :videoId)")
    fun isFavorite(videoId: String): Flow<Boolean>
}