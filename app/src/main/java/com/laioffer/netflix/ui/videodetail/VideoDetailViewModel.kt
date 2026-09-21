package com.laioffer.netflix.ui.videodetail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.laioffer.netflix.datamodel.Episode
import com.laioffer.netflix.datamodel.Video
import com.laioffer.netflix.repository.FavoriteRepository
import com.laioffer.netflix.repository.VideoDetailRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "VideoDetailViewModel"
// Holds Home screen state and asks the repository for data.
@HiltViewModel
class VideoDetailViewModel @Inject constructor(
    private val repository: VideoDetailRepository,
    private val favoriteRepository: FavoriteRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<VideoDetailUiState>(VideoDetailUiState.Loading)
    val uiState: StateFlow<VideoDetailUiState> = _uiState.asStateFlow()

    fun fetchVideoDetail(videoId: String) {
        viewModelScope.launch {
            try {
                _uiState.value = VideoDetailUiState.Loading
                val response = repository.getVideoDetail(videoId)

                _uiState.value = VideoDetailUiState.Success(
                    video = response.video,
                    episodes = response.episodes ?: emptyList()
                )
            } catch (e: Exception) {
                Log.e(TAG, "Failed to load video detail", e)
                _uiState.value = VideoDetailUiState.Error(e.message ?: "Unable to load video detail")
            }
        }

    }

    fun fetchFavoriteStatus(video: Video) {
        viewModelScope.launch {
            val flow: Flow<Boolean> = favoriteRepository.isFavoriteFlow(video.id)
            flow.collect { isFavorite ->
                _uiState.update {
                    if (it is VideoDetailUiState.Success) {
                        it.copy(isFavorite = isFavorite)
                    } else {
                        it
                    }
                }
            }
        }
    }

    fun toggleFavorite() {
        viewModelScope.launch {
            val state = uiState.value as? VideoDetailUiState.Success ?: return@launch
            if (state.isFavorite) {
                favoriteRepository.removeFavorite(state.video.id)
            } else {
                favoriteRepository.addFavorite(state.video)
            }
        }
    }
}

sealed class VideoDetailUiState {
    data object Loading : VideoDetailUiState()
    data class Error(val message: String) : VideoDetailUiState()
    data class Success(
        val video: Video,
        val episodes: List<Episode> = emptyList(),
        val isFavorite: Boolean = false
    ) : VideoDetailUiState()
}