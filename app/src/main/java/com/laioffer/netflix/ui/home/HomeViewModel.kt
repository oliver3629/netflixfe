package com.laioffer.netflix.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.laioffer.netflix.datamodel.Section
import com.laioffer.netflix.datamodel.Video
import com.laioffer.netflix.repository.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "HomeViewModel"

// Holds Home screen state and asks the repository for data.
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: HomeRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    fun fetchHomeFeed() {
        viewModelScope.launch {
            try {
                _uiState.value = HomeUiState.Loading
                val response = repository.getHomeFeed()
                Log.d(TAG, "Loaded home feed: ${response.sections.size} sections")
                _uiState.value = HomeUiState.Success(
                    topRecommended = response.topRecommended,
                    sections = response.sections
                )
            } catch (e: Exception) {
                Log.e(TAG, "Failed to load home feed", e)
                _uiState.value = HomeUiState.Error(e.message ?: "Unable to load home feed")
            }
        }
    }
}

sealed class HomeUiState {
    data object Loading : HomeUiState()
    data class Error(val message: String) : HomeUiState()
    data class Success(val topRecommended: Video?, val sections: List<Section>) : HomeUiState()
}