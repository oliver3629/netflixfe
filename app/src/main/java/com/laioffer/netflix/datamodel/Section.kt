package com.laioffer.netflix.datamodel

// Represents one horizontal row on the future home page.
data class Section(
    val title: String,
    val videos: List<Video>
)