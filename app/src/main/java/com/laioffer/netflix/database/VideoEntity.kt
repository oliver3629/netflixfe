package com.laioffer.netflix.database

import androidx.room.Entity
import androidx.room.PrimaryKey

// Persisted row for one locally saved favorite video.
@Entity(tableName = "favorites")
data class VideoEntity(
    @PrimaryKey val id: String,
    val title: String,
    val posterUrl: String
)