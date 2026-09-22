package com.laioffer.netflix.ui.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.laioffer.netflix.R
import com.laioffer.netflix.database.VideoEntity
import com.laioffer.netflix.datamodel.Profile
import com.laioffer.netflix.datamodel.Video
import com.laioffer.netflix.ui.components.ErrorContent
import com.laioffer.netflix.ui.components.LoadingContent
import com.laioffer.netflix.ui.components.VideoThumbnail
import com.laioffer.netflix.ui.theme.Spacings

// Temporary destination for the My Netflix tab.
@Composable
fun ProfileScreen(
    onVideoClick: (String) -> Unit = {},
    viewModel: ProfileViewModel = hiltViewModel()
) {

    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchProfile()
    }

    LaunchedEffect(uiState is ProfileUiState.Success) {
        if (uiState is ProfileUiState.Success)  {
            viewModel.fetchFavoriteVideos()
        }
    }

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        item {
            ProfileHeader()
        }

        when (val state = uiState) {
            is ProfileUiState.Loading -> item { LoadingContent() }
            is ProfileUiState.Error -> item {
                ErrorContent(
                    message = state.message,
                    onRetry = {viewModel.fetchProfile() }
                )
            }
            is ProfileUiState.Success -> {
                item {
                    ProfileSection(profile = state.profile)
                }
                item {
                    RecentViewedSection(
                        videos = state.recentViewed,
                        onVideoClick = onVideoClick
                    )
                }
                item {
                    FavoriteVideosSection(
                        favorites = state.favorites,
                        onVideoClick = onVideoClick
                    )
                }
            }
        }
    }
}

@Composable
private fun ProfileHeader() {
    Text(
        text = stringResource(R.string.my_netflix),
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.onBackground,
        modifier = Modifier.padding(Spacings.two)
    )
}

@Composable
private fun ProfileSection(profile: Profile) {
    Column (
        modifier = Modifier
            .fillMaxWidth()
            .padding(Spacings.two),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = profile.avatarUrl,
            contentDescription = profile.name,
            modifier = Modifier
                .size(AvatarSize)
                .clip(RoundedCornerShape(AvatarCornerRadius)),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(Spacings.one))
        Row (verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = profile.name,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}

@Composable
private fun RecentViewedSection(
    videos: List<Video>,
    onVideoClick: (String) -> Unit
) {

    if (videos.isEmpty()) return

    Column(
        modifier = Modifier.fillMaxWidth().padding(vertical = Spacings.one)
    ) {
        Text(
            text = stringResource(R.string.recent_viewed),
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(horizontal = Spacings.two, vertical = Spacings.one)
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(Spacings.one),
            modifier = Modifier.padding(horizontal = Spacings.two)
        ) {
            items(videos) { video ->
                VideoThumbnail(
                    title = video.title,
                    posterUrl = video.posterUrl,
                    onClick = { onVideoClick(video.id) }
                )
            }
        }
    }
}

@Composable
private fun FavoriteVideosSection(
    favorites: List<VideoEntity>,
    onVideoClick: (String) -> Unit
) {

    if (favorites.isEmpty()) return

    Column(
        modifier = Modifier.fillMaxWidth().padding(vertical = Spacings.one)
    ) {
        Text(
            text = stringResource(R.string.favorites),
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(horizontal = Spacings.two, vertical = Spacings.one)
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(Spacings.one),
            modifier = Modifier.padding(horizontal = Spacings.two)
        ) {
            items(favorites) { video ->
                VideoThumbnail(
                    title = video.title,
                    posterUrl = video.posterUrl,
                    onClick = { onVideoClick(video.id) }
                )
            }
        }
    }
}

private val AvatarSize = 80.dp
private val AvatarCornerRadius = 8.dp
