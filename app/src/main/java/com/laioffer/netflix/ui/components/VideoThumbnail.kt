package com.laioffer.netflix.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.laioffer.netflix.ui.theme.Spacings

// Poster card used inside horizontal Home rows.
@Composable
fun VideoThumbnail(
    title: String,
    posterUrl: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .width(ThumbnailWidth)
            .clickable(onClick = onClick)
    ) {
        AsyncImage(
            model = posterUrl,
            contentDescription = title,
            modifier = Modifier
                .width(ThumbnailWidth)
                .height(ThumbnailHeight),
            contentScale = ContentScale.Crop
        )
        Text(
            text = title,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(top = Spacings.half),
            maxLines = 2
        )
    }
}

private val ThumbnailWidth = 120.dp
private val ThumbnailHeight = 180.dp