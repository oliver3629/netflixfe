package com.laioffer.netflix.datamodel

// Response returned by POST /videoDetail.
data class VideoDetailResponse(
    val video: Video,
    val episodes: List<Episode>?
)