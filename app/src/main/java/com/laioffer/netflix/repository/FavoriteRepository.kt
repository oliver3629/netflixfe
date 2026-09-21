package com.laioffer.netflix.repository

import com.laioffer.netflix.database.FavoriteDao
import com.laioffer.netflix.database.VideoEntity
import com.laioffer.netflix.datamodel.Video
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

// Repository that hides Room details from the ViewModel.
@Singleton
class FavoriteRepository @Inject constructor(
    private val favoriteDao: FavoriteDao
) {
    // dispatch.io is not necessary here since room handles the thread while creating suspend, mostly remind purpose
    suspend fun addFavorite(video: Video) = withContext(Dispatchers.IO) {
        favoriteDao.insert(
            VideoEntity(
                id = video.id,
                title = video.title,
                posterUrl = video.posterUrl
            )
        )
    }

    suspend fun removeFavorite(videoId: String) {
        favoriteDao.deleteByVideoId(videoId)
    }

    fun isFavoriteFlow(videoId: String): Flow<Boolean> =
        favoriteDao.isFavorite(videoId).flowOn(Dispatchers.IO)
}
