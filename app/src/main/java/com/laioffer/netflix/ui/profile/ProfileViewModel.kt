package com.laioffer.netflix.ui.profile

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.laioffer.netflix.database.VideoEntity
import com.laioffer.netflix.datamodel.Profile
import com.laioffer.netflix.datamodel.Video
import com.laioffer.netflix.repository.FavoriteRepository
import com.laioffer.netflix.repository.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "ProfileViewModel"

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: ProfileRepository,
    private val favoriteRepository: FavoriteRepository
): ViewModel() {

    private val _uiState = MutableStateFlow<ProfileUiState>(ProfileUiState.Loading)
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    fun fetchProfile() {
        viewModelScope.launch {
            try {
                _uiState.value = ProfileUiState.Loading

                val response = repository.getProfile()
                val profile = response.profile ?: error("Profile Not Found")

                Log.d(TAG, "Loaded profile: ${profile.name}")

                _uiState.value = ProfileUiState.Success(
                    profile = profile,
                    recentViewed = response.recentViewed ?: emptyList(),
                )
            } catch (e: Exception) {
                Log.e(TAG, "Failed to fetch profile", e)
                _uiState.value = ProfileUiState.Error(e.message ?: "Unable to load profile")
            }
        }
    }

    fun fetchFavoriteVideos() {
        viewModelScope.launch {
            favoriteRepository.getAllFavorite().collect { favorites ->
                _uiState.update { state ->
                    if (state is ProfileUiState.Success) {
                        state.copy(favorites = favorites)
                    } else {
                        state
                    }
                }
            }
        }
    }

}

sealed class ProfileUiState {
    object Loading : ProfileUiState()

    data class Error(val message: String) : ProfileUiState()

    data class Success(
        val profile: Profile,
        val recentViewed: List<Video> = emptyList(),
        val favorites: List<VideoEntity> = emptyList(),
    ) : ProfileUiState()
}