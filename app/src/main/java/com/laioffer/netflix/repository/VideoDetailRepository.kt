package com.laioffer.netflix.repository

import com.laioffer.netflix.datamodel.VideoDetailRequest
import com.laioffer.netflix.datamodel.VideoDetailResponse
import com.laioffer.netflix.network.NetworkApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

// Data layer object that fetches detail data for one selected video.
@Singleton
class VideoDetailRepository @Inject constructor(
    private val networkApi: NetworkApi
) {
    suspend fun getVideoDetail(videoId: String): VideoDetailResponse = withContext(Dispatchers.IO) {
        val response = networkApi.getDetail(VideoDetailRequest(id = videoId)).execute()

        if (response.isSuccessful) {
            response.body() ?: error("Empty video detail response")
        } else {
            error("Video detail request failed: ${response.code()}")
        }
    }
}