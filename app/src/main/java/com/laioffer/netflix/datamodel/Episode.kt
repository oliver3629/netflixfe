package com.laioffer.netflix.datamodel

// Represents one episode row for a TV show detail page.
data class Episode(
    val id: String,
    val title: String,
    val episodeNumber: Int,
    val duration: String,
    val description: String,
    val videoUrl: String,
    val thumbnailUrl: String
)