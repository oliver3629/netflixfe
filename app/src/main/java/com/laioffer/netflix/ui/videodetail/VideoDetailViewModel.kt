package com.laioffer.netflix.ui.videodetail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.laioffer.netflix.datamodel.Episode
import com.laioffer.netflix.datamodel.Video
import com.laioffer.netflix.repository.VideoDetailRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "VideoDetailViewModel"

// Holds detail screen state and asks the repository for one selected video.
@HiltViewModel
class VideoDetailViewModel @Inject constructor(
    private val repository: VideoDetailRepository
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
}

sealed class VideoDetailUiState {
    data object Loading : VideoDetailUiState()
    data class Error(val message: String) : VideoDetailUiState()
    data class Success(
        val video: Video,
        val episodes: List<Episode> = emptyList()
    ) : VideoDetailUiState()
}