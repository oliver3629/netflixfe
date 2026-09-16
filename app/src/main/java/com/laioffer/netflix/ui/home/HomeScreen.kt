package com.laioffer.netflix.ui.home

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.laioffer.netflix.R
import com.laioffer.netflix.ui.components.ErrorContent
import com.laioffer.netflix.ui.components.LoadingContent
import com.laioffer.netflix.ui.theme.Spacings
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.laioffer.netflix.datamodel.Section
import com.laioffer.netflix.datamodel.Video
import com.laioffer.netflix.ui.components.VideoThumbnail

// Temporary destination for the Home tab.
@Composable
fun HomeScreen(
    onVideoClick: (String) -> Unit = {},
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchHomeFeed()
    }

    HomeScreenContent(
        uiState = uiState,
        onRetry = { viewModel.fetchHomeFeed() },
        onVideoClick = onVideoClick,
        modifier = Modifier.fillMaxSize()
    )
}

// Stateless content that renders whichever HomeUiState the VideModel exposes.
@Composable
private fun HomeScreenContent(
    uiState: HomeUiState,
    onRetry: () -> Unit,
    onVideoClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {

    LazyColumn(
        modifier = modifier
    ) {
        item {
            HomeHeader()
        }

        when (val state = uiState) {
            is HomeUiState.Loading -> item {
                LoadingContent()
            }

            is HomeUiState.Error -> {
                item {
                    ErrorContent(
                        message = state.message,
                        onRetry = onRetry
                    )
                }
            }

            is HomeUiState.Success -> {
                state.topRecommended?.let { video ->
                    item {
                        TopRecommendedBanner(
                            video = video,
                            onPlayClick = { onVideoClick(video.id) }
                        )
                    }
                }

                items(state.sections) { section ->
                    VideoSection(
                        section = section,
                        onVideoClick = onVideoClick,
                    )
                }
            }
        }
    }
}

// Displays the Home tab title above the feed.
@Composable
private fun HomeHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(Spacings.two),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(R.string.home),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}

// Shows the featured backend video at the top of the feed.
@Composable
private fun TopRecommendedBanner(
    video: Video,
    onPlayClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(BannerHeight)
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
                            Color.Black.copy(alpha = 0.7f)
                        )
                    )
                )
        )

        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(Spacings.two)
        ) {
            Text(
                text = video.title,
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(Spacings.two))
            Button(
                onClick = onPlayClick,
                modifier = Modifier.width(PlayButtonWidth)
            ) {
                Text(stringResource(R.string.play), color = MaterialTheme.colorScheme.onPrimary)
            }
        }
    }
}

// Renders one backend section as a title plus horizontal poster row.
@Composable
private fun VideoSection(
    section: Section,
    onVideoClick: (String) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = Spacings.one)
    ) {
        Text(
            text = section.title,
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(horizontal = Spacings.two, vertical = Spacings.one)
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(Spacings.one),
            modifier = Modifier.padding(horizontal = Spacings.two)
        ) {
            items(section.videos) { video ->
                VideoThumbnail(
                    title = video.title,
                    posterUrl = video.posterUrl,
                    onClick = { onVideoClick(video.id) }
                )
            }
        }
    }
}

private val BannerHeight = 400.dp
private val PlayButtonWidth = 120.dp