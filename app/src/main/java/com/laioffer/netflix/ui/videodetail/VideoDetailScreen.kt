package com.laioffer.netflix.ui.videodetail

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.laioffer.netflix.R
import com.laioffer.netflix.datamodel.Episode
import com.laioffer.netflix.datamodel.Video
import com.laioffer.netflix.datamodel.VideoType
import com.laioffer.netflix.ui.components.ErrorContent
import com.laioffer.netflix.ui.components.LoadingContent
import com.laioffer.netflix.ui.theme.Spacings

// Detail screen for one selected movie or TV show.
@Composable
fun VideoDetailScreen(
    videoId: String,
    onBackClick: () -> Unit,
    viewModel: VideoDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(videoId) {
        viewModel.fetchVideoDetail(videoId)
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            VideoDetailHeader(onBackClick = onBackClick)
        }

        when (val state = uiState) {
            is VideoDetailUiState.Loading -> item { LoadingContent() }
            is VideoDetailUiState.Error -> item {
                ErrorContent(
                    message = state.message,
                    onRetry = { viewModel.fetchVideoDetail(videoId) }
                )
            }
            is VideoDetailUiState.Success -> {
                item {
                    VideoDetailPoster(video = state.video)
                }
                item {
                    VideoInfo(video = state.video)
                }
                if (state.video.type == VideoType.MOVIE) {
                    item {
                        PlayButton()
                    }
                }

                item {
                    Text(
                        text = state.video.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.padding(horizontal = Spacings.two, vertical = Spacings.one)
                    )
                }

                if (state.video.type == VideoType.TV_SHOW && state.episodes.isNotEmpty()) {
                    item { EpisodesHeader() }
                    items(state.episodes) { episode ->
                        EpisodeItem(
                            episode = episode,
                            modifier = Modifier.padding(horizontal = Spacings.two, vertical = Spacings.one)
                        )
                    }
                }

            }
        }
    }
}

// Placeholder button for a future playback class.
@Composable
private fun PlayButton() {
    Button(
        onClick = {},
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Spacings.two)
            .height(PlayButtonHeight)
    ) {
        Text(
            text = stringResource(R.string.play),
            color = MaterialTheme.colorScheme.onPrimary,
            style = MaterialTheme.typography.bodyLarge
        )
    }
    Spacer(modifier = Modifier.height(Spacings.two))
}

// Header keeps the brand visible and gives students a back/close action.
@Composable
private fun VideoDetailHeader(onBackClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(Spacings.two),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(R.string.netflix_brand),
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.primary
        )
        Icon(
            imageVector = Icons.Filled.Close,
            contentDescription = stringResource(R.string.close),
            modifier = Modifier
                .size(VideoDetailHeaderIconSize)
                .clickable(onClick = onBackClick),
            tint = MaterialTheme.colorScheme.onBackground
        )
    }
}

// Poster hero image shown at the top of the detail content.
@Composable
private fun VideoDetailPoster(video: Video) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(PosterHeight)
    ) {
        AsyncImage(
            model = video.posterUrl,
            contentDescription = video.title,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.8f)
                        )
                    )
                )
        )
    }
}

// Title and metadata shown below the poster.
@Composable
private fun VideoInfo(video: Video) {
    Column(
        modifier = Modifier.padding(Spacings.two)
    ) {
        Text(
            text = video.title,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(modifier = Modifier.height(Spacings.one))

        val metadataLabels = listOfNotNull(
            video.year,
            video.rating,
            video.duration
        )
        Text(
            text = metadataLabels.joinToString(
                separator = " ${stringResource(R.string.metadata_separator)} "
            ),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}

// Section title shown above TV-show episodes.
@Composable
private fun EpisodesHeader() {
    Text(
        text = stringResource(R.string.episodes),
        style = MaterialTheme.typography.titleSmall,
        color = MaterialTheme.colorScheme.onBackground,
        modifier = Modifier.padding(horizontal = Spacings.two, vertical = Spacings.one)
    )
}

// One row in the TV-show episode list.
@Composable
private fun EpisodeItem(
    episode: Episode,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth()
    ) {
        AsyncImage(
            model = episode.thumbnailUrl,
            contentDescription = episode.title,
            modifier = Modifier
                .width(EpisodeThumbWidth)
                .height(EpisodeThumbHeight),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = Spacings.two),
            verticalArrangement = Arrangement.spacedBy(Spacings.half)
        ) {
            Text(
                text = stringResource(
                    R.string.episode_number_title,
                    episode.episodeNumber,
                    episode.title
                ),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = episode.duration,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = episode.description,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2
            )
        }
    }
}

private val PosterHeight = 300.dp
private val VideoDetailHeaderIconSize = 32.dp
private val PlayButtonHeight = 48.dp
private val EpisodeThumbWidth = 120.dp
private val EpisodeThumbHeight = 80.dp
