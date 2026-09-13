package com.laioffer.netflix.datamodel

enum class VideoType {
    MOVIE,
    TV_SHOW;
}

// Represents one movie or TV show returned by the backend.
data class Video(
    val id: String,
    val title: String,
    val type: VideoType,
    val year: String,
    val rating: String,
    val description: String,
    val posterUrl: String,
    val videoUrl: String,
    val duration: String? = null
)